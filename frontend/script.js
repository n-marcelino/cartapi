document.addEventListener('DOMContentLoaded', () => {
    const apiUrl = 'http://localhost:8080/api/cart';

    const fetchCartItems = async () => {
        try {
            const response = await fetch(apiUrl);
            if (!response.ok) throw new Error('Network response was not ok');
            const cartItems = await response.json();
            const cartItemsList = document.getElementById('cartItems');
            cartItemsList.innerHTML = '';
            cartItems.forEach(item => {
                const li = document.createElement('li');
                li.innerHTML = `${item.name} - Quantity: ${item.quantity} - Price: $${item.price.toFixed(2)}`;
                const deleteButton = document.createElement('button');
                deleteButton.textContent = 'Delete';
                deleteButton.onclick = () => deleteCartItem(item.id);
                li.appendChild(deleteButton);
                cartItemsList.appendChild(li);
            });
        } catch (error) {
            console.error('Error fetching cart items:', error);
        }
    };

    const addCartItem = async (cartItem) => {
        try {
            const response = await fetch(apiUrl, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(cartItem)
            });
            if (!response.ok) throw new Error('Network response was not ok');
            fetchCartItems();
        } catch (error) {
            console.error('Error adding cart item:', error);
        }
    };

    const deleteCartItem = async (id) => {
        try {
            const response = await fetch(`${apiUrl}/${id}`, {
                method: 'DELETE'
            });
            if (!response.ok) throw new Error('Network response was not ok');
            fetchCartItems();
        } catch (error) {
            console.error('Error deleting cart item:', error);
        }
    };

    document.getElementById('addItemForm').addEventListener('submit', (event) => {
        event.preventDefault();
        const name = document.getElementById('name').value;
        const quantity = parseInt(document.getElementById('quantity').value);
        const price = parseFloat(document.getElementById('price').value);
        const cartItem = { name, quantity, price };
        addCartItem(cartItem);
    });

    fetchCartItems();
});
