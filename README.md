# Multitenancy Project README

###  Overview 
This project is designed to provide a comprehensive guide on implementing and utilizing multitenancy using the CASA, TET, FES, and RABAT schemas. It aims to assist developers in understanding the concept of multitenancy and how it can be applied within their applications. 

###  Introduction
<img src="https://github.com/ahdadou/springboot-multi-tenancy/assets/68736304/1c1a95ea-f62e-48a4-bced-1988238e7250" alt="Your image title" width="350"/>

This project is designed to provide a comprehensive guide on implementing and utilizing multitenancy using the CASA, TET, FES, and RABAT schemas. It aims to assist developers in understanding the concept of multitenancy and how it can be applied within their applications.

In this project, we utilize the PostgreSQL database as the underlying data storage system. It is important to have PostgreSQL installed and properly configured before proceeding with the instructions provided in the documentation.

To manage the creation of tables and database schema changes, we employ Liquibase, an open-source database schema management tool. Liquibase allows for versioning, tracking, and executing database schema changes in an organized and controlled manner.

By following the guidelines and best practices outlined in this project, developers will gain a clear understanding of multitenancy concepts and be able to leverage PostgreSQL and Liquibase to efficiently implement and manage multitenancy in their applications.

### Getting Started

 - Clone the repository to your local machine.  
 - Install the necessary dependencies as mentioned in the requirements file.
 - Manually create the required schemas (CASA, TET, FES, RABAT) in your
   database management system after running the project.
 - Familiarize yourself with the best practices section to ensure a
   successful implementation of multitenancy in your application.

   ### How does it work

In this project, we utilize the `TenantContext` class along with `ThreadLocal` to dynamically set the current tenant/schema for data storage. The `ThreadLocal` allows us to maintain separate data contexts for each thread in a multithreaded environment.

Let's take a closer look at the code snippet provided:

     @Bean  
     CommandLineRunner commandLineRunner(AuthorRepository authorRepository) {  
        return args -> {  
	        // ADDING AUTHOR TO CASA SCHEMA  
	        TenantContext.setCurrentTenant(TenantId.CASA);  
	        Author author = new Author(12,"author1");  
	        authorRepository.save(author);  
        };  
      }
In this example, we are using the `CommandLineRunner` interface to execute code at application startup. We set the current tenant/schema to "CASA" using `TenantContext.setCurrentTenant(TenantId.CASA)`. This sets the `ThreadLocal` value for the current thread, ensuring that any subsequent data operations are performed in the CASA schema.

Then, we create an `Author` object and save it using the `authorRepository.save(author)` method. Since the current schema is set to CASA, the `Author` object will be saved in the CASA schema.

By leveraging this approach, you can dynamically switch between different schemas based on your application's requirements. This allows for efficient data isolation and management for each tenant within the multitenant environment.
