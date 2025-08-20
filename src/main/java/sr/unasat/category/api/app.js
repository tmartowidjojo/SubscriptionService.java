const apiUrl = 'http://localhost:8080/api/categories';// API endpoint voor de categorieën

// HTML-elementen ophalen uit de DOM
const categoryList = document.getElementById('category-list');
const categoryForm = document.getElementById('category-form');
const nameInput = document.getElementById('name');
const descriptionInput = document.getElementById('description');
const messageDiv = document.getElementById('message');
const cancelBtn = document.getElementById('cancel-update');

// Functie om een bericht te tonen (bij succes of foutmelding)
function showMessage(text, isError = false) {
    messageDiv.textContent = text;
    messageDiv.style.color = isError ? 'red' : 'green';
    setTimeout(() => {
        messageDiv.textContent = '';
    }, 3000);
}
// Haal alle categorieën op van de API en toon ze in de lijst
function fetchCategories() {
    fetch(apiUrl)
        .then(res => res.json())
        .then(categories => {
            categoryList.innerHTML = '';

            categories.forEach(cat => {
                // Maak een lijstitem aan voor elke categorie
                const li = document.createElement('li');
                li.textContent = `${cat.name} — ${cat.description}`;

                const updateBtn = document.createElement('button');
                updateBtn.textContent = 'Update';
                updateBtn.style.marginLeft = '10px';
                updateBtn.onclick = () => showUpdateForm(cat);

                const deleteBtn = document.createElement('button');
                deleteBtn.textContent = 'Delete';
                deleteBtn.style.marginLeft = '5px';
                deleteBtn.onclick = () => deleteCategory(cat.id);

                li.appendChild(updateBtn);
                li.appendChild(deleteBtn);
                categoryList.appendChild(li);
            });
        })
        .catch(err => {
            console.error('Fetch failed:', err);
            showMessage('Failed to fetch categories', true);
        });
}

function deleteCategory(id) {
    fetch(`${apiUrl}/${id}`, { method: 'DELETE' })
        .then(res => {
            if (res.ok) {
                showMessage('Category deleted');
                fetchCategories();
            } else {
                showMessage('Failed to delete category', true);
            }
        })
        .catch(() => showMessage('Failed to delete category', true));
}

function showUpdateForm(category) {
    nameInput.value = category.name;
    descriptionInput.value = category.description;
    categoryForm.dataset.updateId = category.id;
    categoryForm.querySelector('button[type="submit"]').textContent = 'Update Category';
    cancelBtn.style.display = 'inline-block';
}

function resetForm() {
    categoryForm.reset();
    delete categoryForm.dataset.updateId;
    categoryForm.querySelector('button[type="submit"]').textContent = 'Add';
    cancelBtn.style.display = 'none';
}

cancelBtn.addEventListener('click', resetForm);

categoryForm.addEventListener('submit', event => {
    event.preventDefault();

    const data = {
        name: nameInput.value.trim(),
        description: descriptionInput.value.trim()
    };

    if (!data.name) {
        showMessage('Name is required', true);
        return;
    }

    // Bepaal of het een update of een nieuwe toevoeging is
    const isUpdate = categoryForm.dataset.updateId !== undefined;
    const url = isUpdate ? `${apiUrl}/${categoryForm.dataset.updateId}` : apiUrl;
    const method = isUpdate ? 'PUT' : 'POST';


    // Verstuur POST of PUT request naar API
    fetch(url, {
        method,
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data)
    })
        .then(async res => {
            const responseBody = await res.text();
            console.log('Server response:', res.status, responseBody);

            if (res.ok) {
                showMessage(isUpdate ? 'Category updated' : 'Category added');
                resetForm();
                fetchCategories();
            } else {
                showMessage('Failed to ' + (isUpdate ? 'update' : 'add') + ' category', true);
            }
        })
        .catch(err => {
            console.error('Request failed:', err);
            showMessage('Request failed', true);
        });
});

fetchCategories();
