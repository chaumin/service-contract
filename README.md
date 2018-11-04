# Service Contract

## Technical detail design
This document provide the detail design for Simplified Contract system.

### Requirement
Please refer to Simplified Contract specification -  SR1234568Q, project code P00001

### Design decisions
1. Database: 
    - Production: MySQL 
    - Development: H2

    Both H2 and SQLite is a SQL database that runs in embedded mode, either in memory or saved as a file. The reason we don't use SQLite for development is because Hibernate does not provide a dialect for SQLite and Spring Boot doesn't work out of the box with SQLite (as it does for example with H2, HSQL or Apache Derby) 
2. Using Liquibase to handle sql script.    
3. Backend: Java, Springboot, JPA
4. Front end: Angular 6. Angular 1.x is good but it will be better if we use the newer vrsion for building new application.
5. Using Jhipster to generate the structure of project. JHipster is a development platform to generate, develop and deploy Spring Boot + Angular Web applications and Spring microservices. 

### Low level design
#### Database
##### Table: contract
| Field | Data type | Key | Note |
| ----- | --------- | --- | ---- |
| Id | bigint | PK | Not null, autoIncrement |
| customer_name | varchar(200) |  |  |
| customer_age | integer |  |  |
| customer_doc_id | varchar(64) |  |  |
| content | longblob |  |  |
| content_content_type | varchar(50) |  |  |
| created_by | varchar(50) |  |  |
| created_date | timestamp |  |  |
| last_modified_by | varchar(50) |  | defaultValueComputed="CURRENT_TIMESTAMP" |
| last_modified_date | timestamp |  | defaultValueComputed="CURRENT_TIMESTAMP" |

##### Table: product
| Field | Data type | Key | Note |
| ----- | --------- | --- | ---- |
| Id | bigint | PK | Not null, autoIncrement |
| product_type | varchar(100) |  |  |
| commitment | integer |  |  |
| price | float |  |  |
| name | varchar(255) |  |  |
| device | varchar(100) |  |  |
| service_id | varchar(64) |  |  |
| contract_id | bigint | FK | reference table: contract(id) |

##### Table: vase
| Field | Data type | Key | Note |
| ----- | --------- | --- | ---- |
| Id | bigint | PK | Not null, autoIncrement |
| name | varchar(500) |  |  |
| product_id | bigint | FK | reference table: product(id)  |

 #### APIs
 ##### End point 1: Generate Contract
 ###### API
This api will receive the Customer inforamtion and card detail to generate the contract

| Field | Data type |
| ----- | --------- |
| URL | /api/service-contract |
| Method | POST |
| Request Body | Refer to RequestBody |
| Response Body | refer to ResponseBody |

> RequestBody:
> ```javascript
> {
>   "customerInfo": {
>     "age": 0,
>     "docId": "string",
>     "name": "string"
>   },
>   "products": [
>     {
>       "commitment": 0,
>       "device": "string",
>       "name": "string",
>       "price": 0,
>       "serviceId": "string",
>       "type": "string",
>       "vases": [
>         {
>           "vas": "string"
>         }
>       ]
>     }
>   ]
> }
> ``` 

> Response Body
> ```javascript
> {
>   "content": "string",
>   "contentContentType": "string",
>   "createdDate": "2018-11-03T08:47:48.214Z",
>   "customerAge": 0,
>   "customerDocId": "string",
>   "customerName": "string",
>   "id": 0,
>   "products": [
>     {
>       "commitment": 0,
>       "contract": {},
>       "device": "string",
>       "id": 0,
>       "name": "string",
>       "price": 0,
>       "productType": "string",
>       "serviceId": "string",
>       "vases": [
>         {
>           "id": 0,
>           "name": "string",
>           "product": {}
>         }
>       ]
>     }
>   ]
> }
> ```

###### Sequence Diagram
![](resource/api_post_service_contract.png)
                    

##### End point 2: Retrive list of contract
 ###### API
Get the list of contracts for a customer based on the customer id

| Field | Data type |
| ----- | --------- |
| URL | /api/service-contract |
| Method | GET |
| Request query | customerDocId.equals={Customer Doc Id} |
| Response Body | refer to ResponseBody |


> Response Body
> ```javascript
> [
>   {
>     "content": "string",
>     "contentContentType": "string",
>     "createdDate": "2018-11-03T08:47:48.200Z",
>     "customerAge": 0,
>     "customerDocId": "string",
>     "customerName": "string",
>     "id": 0
>   }
> ]
> ```

###### Sequence Diagram
![](resource/api_get_contract_list.png)
                    

## Development

Before you can build this project, you must install and configure the following dependencies on your machine:

1. [Node.js][]: We use Node to run a development web server and build the project.
   Depending on your system, you can install Node either from source or as a pre-packaged bundle.
2. [Yarn][]: We use Yarn to manage Node dependencies.
   Depending on your system, you can install Yarn either from source or as a pre-packaged bundle.
3. Jdk 1.8.x
4. Maven with latest verson
5. Clone project from [https://github.com/chaumin/service-contract]

After installing Node, you should be able to run the following command to install development tools.
You will only need to run this command when dependencies change in [package.json](package.json).

    yarn install

Run the following commands in two separate terminals to create a blissful development experience where your browser
auto-refreshes when files change on your hard drive.

    ./mvnw
    yarn start

After server up, asscess application by [http://localhost:8080]