
## MySQL Database Design

### **Table: patients**
* id: INT, Primary Key, Auto Increment
* name: VARCHAR(100), Not Null
* email: VARCHAR(100), Unique, Not Null
* password: VARCHAR(255), Not Null
* phone: VARCHAR(20)
* created_at: TIMESTAMP, Default CURRENT_TIMESTAMP

### **Table: doctors**
* id: INT, Primary Key, Auto Increment
* name: VARCHAR(100), Not Null
* specialization: VARCHAR(100), Not Null
* email: VARCHAR(100), Unique, Not Null
* phone: VARCHAR(20)
* created_at: TIMESTAMP, Default CURRENT_TIMESTAMP

### **Table: appointments**
* id: INT, Primary Key, Auto Increment
* doctor_id: INT, Foreign Key → doctors(id), Not Null
* patient_id: INT, Foreign Key → patients(id), Not Null
* appointment_time: DATETIME, Not Null
* duration: INT, Default 60 (minutes)
* status: INT (0 = Scheduled, 1 = Completed, 2 = Cancelled), Default 0

### **Table: admin**
* id: INT, Primary Key, Auto Increment
* username: VARCHAR(50), Unique, Not Null
* password: VARCHAR(255), Not Null

### **Table: doctor_availability**
* id: INT, Primary Key, Auto Increment
* doctor_id: INT, Foreign Key → doctors(id), Not Null
* available_date: DATE, Not Null
* start_time: TIME, Not Null
* end_time: TIME, Not Null
* is_available: BOOLEAN, Default TRUE

### **Table: payments**
* id: INT, Primary Key, Auto Increment
* appointment_id: INT, Foreign Key → appointments(id), Not Null
* amount: DECIMAL(10,2), Not Null
* payment_status: VARCHAR(20) (Pending, Paid, Failed)
* payment_date: TIMESTAMP

## MongoDB Collection Design

### **Collection: prescriptions**
```json
{
  "_id": "ObjectId('65abc123456')",
  "appointmentId": 101,
  "patientId": 12,
  "doctorId": 5,
  "medications": [
    {
      "name": "Paracetamol",
      "dosage": "500mg",
      "frequency": "Every 6 hours"
    },
    {
      "name": "Amoxicillin",
      "dosage": "250mg",
      "frequency": "Twice a day"
    }
  ],
  "doctorNotes": "Patient should rest and stay hydrated.",
  "attachments": [
    {
      "fileName": "lab-report.pdf",
      "fileUrl": "https://clinic-files.com/reports/lab-report.pdf"
    }
  ],
  "tags": ["fever", "infection"],
  "createdAt": "2026-03-19T10:30:00Z"
}
```

### **Collection: feedback**

```json
{
  "_id": "ObjectId('65xyz789')",
  "patientId": 12,
  "doctorId": 5,
  "appointmentId": 101,
  "rating": 4,
  "comments": "Doctor was very helpful and clear.",
  "createdAt": "2026-03-19T12:00:00Z"
}
```
