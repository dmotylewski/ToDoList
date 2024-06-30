document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('todo-form');
    const todoList = document.getElementById('todo-list');
    const completedTodoList = document.getElementById('completed-todo-list');

    form.addEventListener('submit', async (e) => {
        e.preventDefault();

        const title = document.getElementById('title').value;
        const description = document.getElementById('description').value;
        const dueDate = document.getElementById('dueDate').value;

        const todo = {
            title: title,
            description: description,
            dueDate: dueDate
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
            todoList.innerHTML = '';
            completedTodoList.innerHTML = '';

            todos.forEach(todo => {
                const li = document.createElement('li');
                li.innerHTML = `
                    <span class="todo-details">${todo.title} - ${todo.description} - ${todo.dueDate ? new Date(todo.dueDate).toLocaleDateString() : 'No Date'}</span>
                    <div class="todo-buttons">
                        ${!todo.completed ? `<button data-id="${todo.id}" class="complete-btn">Done</button>` : ''}
                        <button data-id="${todo.id}" class="edit-btn">Edit</button>
                    </div>
                `;
                if (todo.completed) {
                    completedTodoList.appendChild(li);
                } else {
                    todoList.appendChild(li);
                }
            });

            document.querySelectorAll('.edit-btn').forEach(button => {
                button.addEventListener('click', (e) => {
                    const id = e.target.getAttribute('data-id');
                    const todo = todos.find(t => t.id == id);
                    if (todo) {
                        document.getElementById('title').value = todo.title;
                        document.getElementById('description').value = todo.description;
                        document.getElementById('dueDate').value = todo.dueDate ? new Date(todo.dueDate).toISOString().split('T')[0] : '';
                        form.onsubmit = async (e) => {
                            e.preventDefault();
                            const updatedTodo = {
                                title: document.getElementById('title').value,
                                description: document.getElementById('description').value,
                                dueDate: document.getElementById('dueDate').value
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
        } catch (error) {
            console.error('Error fetching ToDos:', error);
        }
    }

    loadTodos();
});
