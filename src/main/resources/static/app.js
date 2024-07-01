document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('todo-form');
    const todoList = document.getElementById('todo-list');
    const completedTodoList = document.getElementById('completed-todo-list');
    const categorySelect = document.getElementById('category');

    form.addEventListener('submit', async (e) => {
        e.preventDefault();

        const title = document.getElementById('title').value;
        const description = document.getElementById('description').value;
        const dueDate = document.getElementById('dueDate').value;
        const category = document.getElementById('category').value;

        const todo = {
            title: title,
            description: description,
            dueDate: dueDate,
            category: { id: category }
        };

        try {
            const response = await fetch('http://localhost:8081/api/todos', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(todo),
            });

            if (response.ok) {
                loadTodos();
                form.reset();
            } else {
                alert('Failed to add ToDo');
            }
        } catch (error) {
            console.error('Error adding ToDo:', error);
        }
    });

    async function loadTodos() {
        try {
            const response = await fetch('http://localhost:8081/api/todos');
            if (!response.ok) {
                throw new Error('Failed to fetch todos');
            }
            const todos = await response.json();

            // Sort todos by due date
            todos.sort((a, b) => {
                const dateA = new Date(a.dueDate);
                const dateB = new Date(b.dueDate);
                return dateA - dateB;
            });

            todoList.innerHTML = '';
            completedTodoList.innerHTML = '';

            todos.forEach(todo => {
                const li = document.createElement('li');
                li.innerHTML = `
                    <span>${todo.title} - ${todo.description} - ${todo.dueDate ? new Date(todo.dueDate).toLocaleDateString() : 'No Date'} - ${todo.category ? todo.category.name : 'No Category'}</span>
                    <button data-id="${todo.id}" class="edit-btn">Edit</button>
                    <button data-id="${todo.id}" class="complete-btn">Done</button>
                    <button data-id="${todo.id}" class="delete-btn">Delete</button>
                    <ul class="comments"></ul>
                `;
                if (todo.completed) {
                    completedTodoList.appendChild(li);
                } else {
                    todoList.appendChild(li);
                }

                const commentsUl = li.querySelector('.comments');
                todo.comments.forEach(comment => {
                    const commentLi = document.createElement('li');
                    commentLi.textContent = comment.text;
                    commentsUl.appendChild(commentLi);
                });

                const addCommentForm = document.createElement('form');
                addCommentForm.innerHTML = `
                    <input type="text" class="comment-input" placeholder="Add a comment" required>
                    <button type="submit">Add</button>
                `;
                addCommentForm.addEventListener('submit', async (e) => {
                    e.preventDefault();
                    const commentText = addCommentForm.querySelector('.comment-input').value;
                    const comment = { text: commentText };

                    try {
                        const response = await fetch(`http://localhost:8081/api/todos/${todo.id}/comments`, {
                            method: 'POST',
                            headers: {
                                'Content-Type': 'application/json',
                            },
                            body: JSON.stringify(comment),
                        });

                        if (response.ok) {
                            loadTodos();
                        } else {
                            alert('Failed to add comment');
                        }
                    } catch (error) {
                        console.error('Error adding comment:', error);
                    }
                });
                li.appendChild(addCommentForm);
            });

            document.querySelectorAll('.edit-btn').forEach(button => {
                button.addEventListener('click', (e) => {
                    const id = e.target.getAttribute('data-id');
                    const todo = todos.find(t => t.id == id);
                    if (todo) {
                        document.getElementById('title').value = todo.title;
                        document.getElementById('description').value = todo.description;
                        document.getElementById('dueDate').value = todo.dueDate ? new Date(todo.dueDate).toISOString().split('T')[0] : '';
                        document.getElementById('category').value = todo.category ? todo.category.id : '';
                        form.onsubmit = async (e) => {
                            e.preventDefault();
                            const updatedTodo = {
                                title: document.getElementById('title').value,
                                description: document.getElementById('description').value,
                                dueDate: document.getElementById('dueDate').value,
                                category: { id: document.getElementById('category').value }
                            };
                            try {
                                const response = await fetch(`http://localhost:8081/api/todos/${id}`, {
                                    method: 'PUT',
                                    headers: {
                                        'Content-Type': 'application/json',
                                    },
                                    body: JSON.stringify(updatedTodo),
                                });

                                if (response.ok) {
                                    loadTodos();
                                    form.reset();
                                    form.onsubmit = null;
                                } else {
                                    alert('Failed to update ToDo');
                                }
                            } catch (error) {
                                console.error('Error updating ToDo:', error);
                            }
                        };
                    }
                });
            });

            document.querySelectorAll('.complete-btn').forEach(button => {
                button.addEventListener('click', async (e) => {
                    const id = e.target.getAttribute('data-id');
                    try {
                        const response = await fetch(`http://localhost:8081/api/todos/${id}/complete`, {
                            method: 'PUT',
                        });

                        if (response.ok) {
                            loadTodos();
                        } else {
                            alert('Failed to mark ToDo as completed');
                        }
                    } catch (error) {
                        console.error('Error marking ToDo as completed:', error);
                    }
                });
            });

            document.querySelectorAll('.delete-btn').forEach(button => {
                button.addEventListener('click', async (e) => {
                    const id = e.target.getAttribute('data-id');
                    try {
                        const response = await fetch(`http://localhost:8081/api/todos/${id}`, {
                            method: 'DELETE',
                        });

                        if (response.ok) {
                            loadTodos();
                        } else {
                            alert('Failed to delete ToDo');
                        }
                    } catch (error) {
                        console.error('Error deleting ToDo:', error);
                    }
                });
            });
        } catch (error) {
            console.error('Error fetching ToDos:', error);
        }
    }

    async function loadCategories() {
        try {
            const response = await fetch('http://localhost:8081/api/todos/categories');
            if (!response.ok) {
                throw new Error('Failed to fetch categories');
            }
            const categories = await response.json();
            categorySelect.innerHTML = ''; // Clear existing options
            categories.forEach(category => {
                const option = document.createElement('option');
                option.value = category.id;
                option.textContent = category.name;
                categorySelect.appendChild(option);
            });
        } catch (error) {
            console.error('Error fetching categories:', error);
        }
    }

    loadTodos();
    loadCategories();
});
