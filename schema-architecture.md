This Spring Boot application uses both MVC and REST controllers. Thymeleaf templates are used for the Admin and Doctor dashboards, while REST APIs serve all other modules. The application interacts with two databases—MySQL (for patient, doctor, appointment, and admin data) and MongoDB (for prescriptions). All controllers route requests through a common service layer, which in turn delegates to the appropriate repositories. MySQL uses JPA entities while MongoDB uses document models.

1. User accesses AdminDashboard or Appointment pages.
2. The action is routed to the appropriate Thymeleaf or REST controller.
3. The controller calls the service layer.
4. The service layer communicates with the Respository Layer to perform data access operations.\
5. Each repository interface directly with the underlying database engine(MySQL which stores all core entities & MongoDB which stores prescriptions)
6. Model Binding : Data retrieved from the database is mapped into Java model classes
7. Bound models are used in the response layer(eg. model passed to Thymeleaf templates to rendered as dynamic HTML/serialized into JSON & sent back to client as part of HTTP response)
