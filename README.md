
# Multitenancy Project README

###  Overview 
This project is designed to provide a comprehensive guide on implementing and utilizing multitenancy using the CASA, TET, FES, and RABAT schemas. It aims to assist developers in understanding the concept of multitenancy and how it can be applied within their applications. 

###  Introduction

<img src="https://github.com/ahdadou/springboot-multi-tenancy/assets/68736304/326af24b-eac1-4fff-862b-cbe3c6078f51" alt="Your image title" width="350"/>

In this project, we utilize the PostgreSQL database as the underlying data storage system. It is important to have PostgreSQL installed and properly configured before proceeding with the instructions provided in the documentation.

To manage the creation of tables and database schema changes, we employ Liquibase, an open-source database schema management tool. Liquibase allows for versioning, tracking, and executing database schema changes in an organized and controlled manner.

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

#####  Switching Between Multi-Tenant and Normal Database Configurations
Also you can switch between multi-tenant and normal database configurations by modifying the `application.properties` file.

Let's take a look at the relevant configuration:

    application:  
	    persistence:  
		    database_urls:  
			    CASA: postgres://postgres:postgres@localhost:5432/stock?schema=CASA  
			    FES: postgres://postgres:postgres@localhost:5432/stock?schema=FES  
		    database_url: postgres://postgres:postgres@localhost:5432/stock?schema=FES  
		    multi-tenancy: true

By setting `application.persistence.multi-tenancy` to `true`, you enable the multi-tenant configuration. This indicates that the application should operate in a multi-tenant mode where the schema is dynamically determined based on the current tenant.

In the `application.persistence.database_urls` section, you can define the database URLs for each schema. For example, `CASA` and `FES` schemas have their respective URLs specified with the schema names included as parameters. These URLs are used to establish connections with the respective schemas when the application is running in multi-tenant mode.

Furthermore, the `application.persistence.database_url` property represents the database URL to be used in the normal database mode (when multi-tenancy is disabled). In this example, the URL corresponds to the `FES` schema.

By adjusting these configurations in the `application.properties` file, you can seamlessly switch between multi-tenant and normal database configurations based on your requirements.

####  Describing Code and Class Techniques

Throughout the project, several techniques are employed to enhance functionality and maintain a structured codebase. Here are some notable examples:

1.  **DataSourceProperties:** The `DataSourceProperties` class plays a crucial role in converting and extracting information from the `database_url` into a dedicated object. By utilizing the Java `URL` class and regular expressions, the `database_url` is deconstructed to obtain the necessary details such as username, password, JDBC URL, driver class name, and schema. This ensures that the database properties are organized and easily accessible for establishing connections.
    
2.  **PersistenceConfig, MultiTenantDataSource, and LiquibaseUtil:** These classes make use of Liquibase, an open-source database schema management tool, to handle database migrations and creation. The `PersistenceConfig` class sets up the necessary configurations for the persistence layer, while the `MultiTenantDataSource` class provides a customized data source implementation that handles tenant-specific connections. Additionally, `LiquibaseUtil` assists in executing changesets defined in resource files, enabling seamless database migration and version control.
    
3.  **TenantFilter:** The `TenantFilter` class implements a servlet filter that intercepts incoming requests and sets the `TenantContext` based on the `X-TenantID` header provided by the user. This technique ensures that each request is associated with the correct tenant, facilitating data isolation and tenant-specific operations throughout the application.
