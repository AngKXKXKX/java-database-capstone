# User Story Template

##### **Admin User Stories**

### **User Story 1**
**Title:**
*As an admin, I want to log into the portal using my username and password, so that I can securely access the system.*

**Acceptance Criteria:**
1. Admin can enter valid username and password to log in successfully
2. System displays an error message for invalid login credentials
3. Session is created upon successful login

**Priority:** High
**Story Points:** 3
**Notes:**
Include basic authentication and validation

### **User Story 2**
**Title:**
*As an admin, I want to log out of the portal, so that I can protect system access.*

**Acceptance Criteria:**
1. Admin can click a logout button to end the session
2. System invalidates the current session after logout
3. Admin is redirected to the login page

**Priority:** High
**Story Points:** 2
**Notes:**
Ensure session timeout handling as an edge case

### **User Story 3**
**Title:**
*As an admin, I want to add doctors to the portal, so that I can manage available healthcare providers.*

**Acceptance Criteria:**
1. Admin can input doctor details (name, specialization, contact info)
2. System validates required fields before saving
3. New doctor is successfully stored and displayed in the system

**Priority:** High
**Story Points:** 5
**Notes:**
Consider duplicate doctor entries

### **User Story 4**
**Title:**
*As an admin, I want to delete a doctor's profile from the portal, so that I can remove inactive or incorrect records.*

**Acceptance Criteria:**
1. Admin can select a doctor profile to delete
2. System asks for confirmation before deletion
3. Doctor profile is permanently removed after confirmation

**Priority:** Medium
**Story Points:** 3
**Notes:**
Handle cases where doctor has existing appointments


### **User Story 5**
**Title:**
*As an admin, I want to run a stored procedure in MySQL CLI to get the number of appointments per month, so that I can track usage statistics.*

**Acceptance Criteria:**
1. Stored procedure returns monthly appointment counts
2. Admin can execute the procedure via MySQL CLI
3. Output is displayed in a readable format

**Priority:** Medium
**Story Points:** 5
**Notes:**
Consider exporting results for reporting

##### **Patients User Stories**

### **User Story 1**
**Title:**
*As a patient, I want to view a list of doctors without logging in, so that I can explore options before registering.*

**Acceptance Criteria:**
1. Patient can access the doctor list without authentication
2. System displays doctor details (name, specialization, availability)
3. No booking actions are allowed without login

**Priority:** Medium
**Story Points:** 3
**Notes:**
* Ensure limited access for non-logged-in users

### **User Story 2**
**Title:**
*As a patient, I want to sign up using my email and password, so that I can book appointments.*

**Acceptance Criteria:**
1. Patient can register with a valid email and password
2. System validates email format and password strength
3. Account is created successfully and stored in the system

**Priority:** High
**Story Points:** 5
**Notes:**
Consider email verification as an enhancement

### **User Story 3**
**Title:**
*As a patient, I want to log into the portal, so that I can manage my bookings.*

**Acceptance Criteria:**
1. Patient can log in using registered credentials
2. System shows an error for invalid login details
3. Patient is redirected to dashboard upon successful login

**Priority:** High
**Story Points:** 3
**Notes:**
Include session management

### **User Story 4**
**Title:**
*As a patient, I want to log out of the portal, so that I can secure my account.*

**Acceptance Criteria:**
1. Patient can click logout to end the session
2. System invalidates session after logout
3. Patient is redirected to login or homepage

**Priority:** High
**Story Points:** 2
**Notes:**
Handle auto logout for inactivity

### **User Story 5**
**Title:**
*As a patient, I want to book an hour-long appointment with a doctor, so that I can consult them.*
**Acceptance Criteria:*
1. Patient can select a doctor and available time slot
2. Appointment duration is fixed at one hour
3. System confirms booking and prevents double-booking

**Priority:** High
**Story Points:** 5
**Notes:**
Handle time conflicts 

### **User Story 6**
**Title:**
*As a patient, I want to view my upcoming appointments, so that I can prepare accordingly.*

**Acceptance Criteria:**
1. Patient can view a list of upcoming appointments
2. Each appointment displays date, time, and doctor details
3. Past appointments are separated from upcoming ones

**Priority:** Medium
**Story Points:** 3
**Notes:**
Consider adding reminder notifications as a future feature


##### **Doctors User Stories**

### **User Story 1**
**Title:**
*As a doctor, I want to log into the portal, so that I can manage my appointments.*

**Acceptance Criteria:**
1. Doctor can log in using valid credentials
2. System displays an error message for invalid login details
3. Doctor is redirected to their dashboard upon successful login

**Priority:** High
**Story Points:** 3
**Notes:**
Ensure secure authentication

### **User Story 2**
**Title:**
*As a doctor, I want to log out of the portal, so that I can protect my data.*

**Acceptance Criteria:**
1. Doctor can click logout to end the session
2. System invalidates the active session
3. Doctor is redirected to the login page

**Priority:** High
**Story Points:** 2
**Notes:**
Include session timeout for inactivity

### **User Story 3**
**Title:**
*As a doctor, I want to view my appointment calendar, so that I can stay organized.*

**Acceptance Criteria:**
1. Doctor can view appointments in a calendar or list format
2. Appointments display date, time, and patient name
3. Calendar updates in real-time when new bookings are made

**Priority:** High
**Story Points:** 5
**Notes:**
 Consider weekly and monthly views

### **User Story 4**
**Title:**
*As a doctor, I want to mark my unavailability, so that patients only see available slots.*

**Acceptance Criteria:**
1. Doctor can select dates/times to mark as unavailable
2. System prevents booking during unavailable periods
3. Changes are reflected immediately in patient booking view

**Priority:** High
**Story Points:** 5
**Notes:**
Handle recurring unavailability (e.g., weekends, holidays)

### **User Story 5**
**Title:**
*As a doctor, I want to update my profile with specialization and contact information, so that patients have up-to-date information.*

**Acceptance Criteria:**
1. Doctor can edit profile details (specialization, contact info)
2. System validates required fields before saving
3. Updated information is visible to patients

**Priority:** Medium
**Story Points:** 3
**Notes:**

### **User Story 6**
**Title:**
*As a doctor, I want to view patient details for upcoming appointments, so that I can be prepared.*

**Acceptance Criteria:**
1. Doctor can access patient details from appointment list
2. Patient information includes name and relevant booking details
3. Access is restricted to only assigned appointments

**Priority:** High
**Story Points:** 5
**Notes:**
Ensure compliance with data privacy regulations



