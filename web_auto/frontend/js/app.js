document.addEventListener('DOMContentLoaded', () => {
  // Login Logic
  const loginForm = document.getElementById('login-form');
  if (loginForm) {
    loginForm.addEventListener('submit', (e) => {
      e.preventDefault();
      const user = document.getElementById('username').value;
      const pass = document.getElementById('password').value;
      const err = document.querySelector('.error-message');
      
      if (user === 'testuser' && pass === 'testpass') {
        err.style.display = 'none';
        localStorage.setItem('isAuthenticated', 'true');
        window.location.href = 'dashboard.html';
      } else {
        err.style.display = 'block';
        err.textContent = 'Invalid credentials. Use testuser/testpass.';
      }
    });
  }

  // Logout Logic
  const logoutBtn = document.getElementById('logout-btn');
  if (logoutBtn) {
    logoutBtn.addEventListener('click', (e) => {
      e.preventDefault();
      localStorage.removeItem('isAuthenticated');
      window.location.href = 'index.html';
    });
  }

  // Auth Check (Except Login)
  if (!window.location.pathname.endsWith('index.html') && !window.location.pathname.endsWith('/')) {
    if (localStorage.getItem('isAuthenticated') !== 'true') {
      window.location.href = 'index.html';
    }
  }

  // Form Page Logic
  const dataForm = document.getElementById('data-form');
  if (dataForm) {
    dataForm.addEventListener('submit', (e) => {
      e.preventDefault();
      
      // Get form values
      const name = document.getElementById('full-name').value;
      const email = document.getElementById('email').value;
      const age = parseInt(document.getElementById('age').value, 10);
      const phone = document.getElementById('phone').value;
      const deptSelect = document.getElementById('department');
      const dept = deptSelect.options[deptSelect.selectedIndex].text;
      
      const newStudent = {
        name: name,
        email: email,
        age: age,
        phone: phone,
        department: dept
      };

      // Send to real backend
      fetch('http://localhost:8080/students/batch', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify([newStudent]) // endpoint expects array
      }).then(res => {
        if (res.ok) {
          const successMsg = document.querySelector('.success-message');
          successMsg.style.display = 'block';
          successMsg.textContent = 'Student successfully added to the database!';
          setTimeout(() => { successMsg.style.display = 'none'; }, 3000);
          dataForm.reset();
        } else {
          alert('Failed to save student.');
        }
      }).catch(err => {
        console.error(err);
        alert('Network error while saving student.');
      });
    });
  }

  // Dynamic Content Logic
  const loadBtn = document.getElementById('load-btn');
  if (loadBtn) {
    loadBtn.addEventListener('click', () => {
      document.getElementById('spinner').style.display = 'inline-block';
      setTimeout(() => {
        document.getElementById('spinner').style.display = 'none';
        const dynamicContent = document.getElementById('dynamic-content');
        dynamicContent.innerHTML = '<p data-test="loaded-text">Content has been loaded dynamically after a delay!</p>';
      }, 2000);
    });
  }

  // Modal Logic
  const openModalBtn = document.getElementById('open-modal');
  const closeModalBtn = document.getElementById('close-modal');
  const modal = document.getElementById('my-modal');
  if (openModalBtn) {
    openModalBtn.addEventListener('click', () => {
      modal.classList.add('active');
    });
    closeModalBtn.addEventListener('click', () => {
      modal.classList.remove('active');
    });
  }

  // Table Logic
  const tableBody = document.querySelector('#data-table tbody');
  if (tableBody) {
    // Load dynamically from backend
    fetch('http://localhost:8080/students')
      .then(res => res.json())
      .then(students => {
        students.forEach(student => {
          const tr = document.createElement('tr');
          tr.innerHTML = `
            <td>${student.id || '-'}</td>
            <td>${student.name || '-'}</td>
            <td>${student.email || '-'}</td>
            <td>${student.age || '-'}</td>
            <td>${student.phone || '-'}</td>
            <td>${student.department || '-'}</td>
          `;
          tableBody.appendChild(tr);
        });
      })
      .catch(err => console.error('Error fetching students:', err));
  }

  const searchInput = document.getElementById('search-input');
  if (searchInput) {
    searchInput.addEventListener('input', (e) => {
      const term = e.target.value.toLowerCase();
      const rows = document.querySelectorAll('tbody tr');
      rows.forEach(row => {
        const text = row.textContent.toLowerCase();
        if (text.includes(term)) {
          row.style.display = '';
        } else {
          row.style.display = 'none';
        }
      });
    });
  }
});
