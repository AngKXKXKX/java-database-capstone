// header.js

function renderHeader() {
    const headerDiv = document.getElementById("header");

    // If user is on the root page, reset role and render basic header
    if (window.location.pathname.endsWith("/")) {
        localStorage.removeItem("userRole");
        localStorage.removeItem("token");
        headerDiv.innerHTML = `
        <header class="header">
            <div class="logo-section">
                <img src="../assets/images/logo/logo.png" alt="Hospital CRM Logo" class="logo-img">
                <span class="logo-title">Hospital CMS</span>
            </div>
        </header>`;
        return;
    }

    const role = localStorage.getItem("userRole");
    const token = localStorage.getItem("token");

    // Basic header with logo
    let headerContent = `
    <header class="header">
        <div class="logo-section">
            <img src="../assets/images/logo/logo.png" alt="Hospital CRM Logo" class="logo-img">
            <span class="logo-title">Hospital CMS</span>
        </div>
        <nav>`;

    // Session check: role exists but no token => logout
    if ((role === "loggedPatient" || role === "admin" || role === "doctor") && !token) {
        localStorage.removeItem("userRole");
        alert("Session expired or invalid login. Please log in again.");
        window.location.href = "/"; // redirect to home or login page
        return;
    }

    // Role-specific buttons
    switch (role) {
        case "admin":
            headerContent += `
            <button id="addDocBtn" class="adminBtn" onclick="openModal('addDoctor')">Add Doctor</button>
            <a href="#" onclick="logout()">Logout</a>`;
            break;

        case "doctor":
            headerContent += `
            <button class="adminBtn" onclick="selectRole('doctor')">Home</button>
            <a href="#" onclick="logout()">Logout</a>`;
            break;

        case "patient":
            headerContent += `
            <button id="patientLogin" class="adminBtn">Login</button>
            <button id="patientSignup" class="adminBtn">Sign Up</button>`;
            break;

        case "loggedPatient":
            headerContent += `
            <button id="home" class="adminBtn" onclick="window.location.href='/pages/loggedPatientDashboard.html'">Home</button>
            <button id="patientAppointments" class="adminBtn" onclick="window.location.href='/pages/patientAppointments.html'">Appointments</button>
            <a href="#" onclick="logoutPatient()">Logout</a>`;
            break;

        default:
            // Fallback header (no role)
            headerContent += ``;
    }

    headerContent += `</nav></header>`;

    // Render the header
    headerDiv.innerHTML = headerContent;

    // Attach event listeners to dynamically created buttons
    attachHeaderButtonListeners();
}

/**
 * Attach click listeners to dynamically rendered header buttons
 */
function attachHeaderButtonListeners() {
    const loginBtn = document.getElementById("patientLogin");
    const signupBtn = document.getElementById("patientSignup");

    if (loginBtn) {
        loginBtn.addEventListener("click", () => openModal("loginPatient"));
    }

    if (signupBtn) {
        signupBtn.addEventListener("click", () => openModal("signupPatient"));
    }
}

/**
 * Logout admin or doctor
 */
function logout() {
    localStorage.removeItem("userRole");
    localStorage.removeItem("token");
    window.location.href = "/";
}

/**
 * Logout patient
 */
function logoutPatient() {
    localStorage.removeItem("userRole");
    localStorage.removeItem("token");
    window.location.href = "/";
}

/**
 * Open modal with a given ID
 * @param {string} modalId
 */
function openModal(modalId) {
    const modal = document.getElementById("modal");
    const modalBody = document.getElementById("modal-body");
    // Load modal content dynamically based on modalId
    modalBody.innerHTML = `<!-- Load content for ${modalId} -->`;
    modal.style.display = "block";
}

/**
 * Role selection function for doctor/admin
 */
function selectRole(role) {
    localStorage.setItem("userRole", role);
    window.location.reload();
}

// Initialize header on page load
document.addEventListener("DOMContentLoaded", renderHeader);