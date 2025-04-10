# CineUNA - Project README

## Overview

CineUNA is a cinema management application designed to streamline various aspects of cinema operations. This project aims to provide a user-friendly interface for managing movies, showtimes, cinema rooms, users, and potentially more features in the future.

This README provides a general overview of the project and its current state. For more detailed information on specific features or setup instructions, please refer to the relevant documentation or code comments.

## Current Features

As of the current development stage, CineUNA includes the following key functionalities:

* **User Management:**
    * User registration with input validation.
    * Secure password handling (Note: Ensure proper password hashing is implemented for production).
    * Account activation via email.
    * Basic internationalization support (Spanish and English).
* **Showtime Management:**
    * Registration of new showtimes, including:
        * Showtime ID
        * Ticket price
        * Start and end times
        * Date
        * Language (English/Spanish)
        * Association with a specific movie.
        * Association with a specific cinema room.
    * Searching for existing showtimes.
    * Updating showtime information.
    * Deleting showtime records.
* **Movie Management (Likely in Progress):**
    * Functionality to search and associate movies with showtimes. (Details of full movie management, such as adding new movies, may be in development or a future feature).
* **Cinema Room Management (Likely in Progress):**
    * Functionality to search and associate cinema rooms with showtimes. (Details of full cinema room management, such as adding new rooms, may be in development or a future feature).
* **Internationalization:**
    * Basic support for Spanish and English languages, applied to user registration and showtime management interfaces.

## Technologies Used

The CineUNA project utilizes the following technologies:

* **Java:** The primary programming language.
* **JavaFX:** A GUI toolkit for building desktop applications.
* **JFoenix:** A JavaFX library providing Material Design UI controls.
* **FXML:** An XML-based language for defining the user interface layout.
* **Jakarta Mail (javax.mail):** Library for sending emails (used for account activation).
* **Potentially:**
    * A database (e.g., MySQL, PostgreSQL) for data persistence.
    * A web server (e.g., Tomcat, Jetty) if any web services are involved.
    * Other libraries for specific functionalities.

## Setup Instructions (General)

While detailed setup instructions may vary depending on the development environment and database configuration, here's a general outline:

1.  **Install Java Development Kit (JDK):** Ensure you have a compatible version of the JDK installed on your system.
2.  **Install a Java IDE (Optional but Recommended):** IntelliJ IDEA, Eclipse, or NetBeans can greatly simplify development.
3.  **Clone the Repository:** Obtain the project code from its source (e.g., Git repository).
4.  **Set up the Database (If Applicable):** If the application uses a database, install and configure it. Create the necessary database schema and tables.
5.  **Configure Database Connection:** Update any configuration files (e.g., properties files) with the database connection details.
6.  **Build the Project:** Use your IDE or a build tool (e.g., Maven, Gradle) to build the project.
7.  **Run the Application:** Execute the main application class to start CineUNA.

**Note:** More specific setup instructions will be provided in a dedicated `INSTALL.md` file or within the project documentation as it progresses.

## Contributing

Contributions to the CineUNA project are welcome. Please follow these general guidelines:

1.  **Fork the Repository:** Create your own fork of the project.
2.  **Create a Branch:** Make your changes in a separate branch.
3.  **Follow Coding Standards:** Adhere to the project's coding style and conventions.
4.  **Write Tests:** Include unit and integration tests for your changes.
5.  **Submit a Pull Request:** Once your changes are complete and tested, submit a pull request to the main repository.



## Future Enhancements (Potential)

The CineUNA project has the potential for further development with features such as:

* **Full Movie Management:** Adding, editing, and deleting movie information.
* **Full Cinema Room Management:** Adding, editing, and deleting cinema room details (capacity, etc.).
* **Showtime Scheduling and Display:** More advanced scheduling tools and user-facing displays of showtimes.
* **Seat Reservation System:** Allowing users to reserve seats for specific showtimes.
* **Ticket Sales and Management:** Integrating a system for selling and managing tickets.
* **User Roles and Permissions:** Implementing different user roles (administrator, staff) with varying levels of access.
* **Reporting and Analytics:** Generating reports on sales, attendance, etc.
* **Enhanced Internationalization:** Supporting more languages.
* **User Interface Improvements:** Further refining the user experience.

## Contact

Cristhofer Vindas
vindaz4567@gmail.com

---
**Last Updated:** November 10, 2022
