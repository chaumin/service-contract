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
| URL | /service-contract |
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
                    
```seq
Andrew->China: Says Hello 
Note right of China: China thinks\nabout it 
China-->Andrew: How are you? 
Andrew->>China: I am good thanks!
```

## Development

Before you can build this project, you must install and configure the following dependencies on your machine:

1. [Node.js][]: We use Node to run a development web server and build the project.
   Depending on your system, you can install Node either from source or as a pre-packaged bundle.
2. [Yarn][]: We use Yarn to manage Node dependencies.
   Depending on your system, you can install Yarn either from source or as a pre-packaged bundle.

After installing Node, you should be able to run the following command to install development tools.
You will only need to run this command when dependencies change in [package.json](package.json).

    yarn install

We use yarn scripts and [Webpack][] as our build system.

Run the following commands in two separate terminals to create a blissful development experience where your browser
auto-refreshes when files change on your hard drive.

    ./mvnw
    yarn start

[Yarn][] is also used to manage CSS and JavaScript dependencies used in this application. You can upgrade dependencies by
specifying a newer version in [package.json](package.json). You can also run `yarn update` and `yarn install` to manage dependencies.
Add the `help` flag on any command to see how you can use it. For example, `yarn help update`.

The `yarn run` command will list all of the scripts available to run for this project.

### Service workers

Service workers are commented by default, to enable them please uncomment the following code.

* The service worker registering script in index.html

```html
<script>
    if ('serviceWorker' in navigator) {
        navigator.serviceWorker
        .register('./service-worker.js')
        .then(function() { console.log('Service Worker Registered'); });
    }
</script>
```

Note: workbox creates the respective service worker and dynamically generate the `service-worker.js`

### Managing dependencies

For example, to add [Leaflet][] library as a runtime dependency of your application, you would run following command:

    yarn add --exact leaflet

To benefit from TypeScript type definitions from [DefinitelyTyped][] repository in development, you would run following command:

    yarn add --dev --exact @types/leaflet

Then you would import the JS and CSS files specified in library's installation instructions so that [Webpack][] knows about them:
Edit [src/main/webapp/app/vendor.ts](src/main/webapp/app/vendor.ts) file:
~~~
import 'leaflet/dist/leaflet.js';
~~~

Edit [src/main/webapp/content/css/vendor.css](src/main/webapp/content/css/vendor.css) file:
~~~
@import '~leaflet/dist/leaflet.css';
~~~
Note: there are still few other things remaining to do for Leaflet that we won't detail here.

For further instructions on how to develop with JHipster, have a look at [Using JHipster in development][].

### Using angular-cli

You can also use [Angular CLI][] to generate some custom client code.

For example, the following command:

    ng generate component my-component

will generate few files:

    create src/main/webapp/app/my-component/my-component.component.html
    create src/main/webapp/app/my-component/my-component.component.ts
    update src/main/webapp/app/app.module.ts


## Building for production

To optimize the ServiceContract application for production, run:

    ./mvnw -Pprod clean package

This will concatenate and minify the client CSS and JavaScript files. It will also modify `index.html` so it references these new files.
To ensure everything worked, run:

    java -jar target/*.war

Then navigate to [http://localhost:8080](http://localhost:8080) in your browser.

Refer to [Using JHipster in production][] for more details.

## Testing

To launch your application's tests, run:

    ./mvnw clean test

### Client tests

Unit tests are run by [Jest][] and written with [Jasmine][]. They're located in [src/test/javascript/](src/test/javascript/) and can be run with:

    yarn test



For more information, refer to the [Running tests page][].

## Using Docker to simplify development (optional)

You can use Docker to improve your JHipster development experience. A number of docker-compose configuration are available in the [src/main/docker](src/main/docker) folder to launch required third party services.

For example, to start a mysql database in a docker container, run:

    docker-compose -f src/main/docker/mysql.yml up -d

To stop it and remove the container, run:

    docker-compose -f src/main/docker/mysql.yml down

You can also fully dockerize your application and all the services that it depends on.
To achieve this, first build a docker image of your app by running:

    ./mvnw verify -Pprod dockerfile:build dockerfile:tag@version dockerfile:tag@commit

Then run:

    docker-compose -f src/main/docker/app.yml up -d

For more information refer to [Using Docker and Docker-Compose][], this page also contains information on the docker-compose sub-generator (`jhipster docker-compose`), which is able to generate docker configurations for one or several JHipster applications.

## Continuous Integration (optional)

To configure CI for your project, run the ci-cd sub-generator (`jhipster ci-cd`), this will let you generate configuration files for a number of Continuous Integration systems. Consult the [Setting up Continuous Integration][] page for more information.

[JHipster Homepage and latest documentation]: https://www.jhipster.tech
[JHipster 5.2.1 archive]: https://www.jhipster.tech/documentation-archive/v5.2.1

[Using JHipster in development]: https://www.jhipster.tech/documentation-archive/v5.2.1/development/
[Using Docker and Docker-Compose]: https://www.jhipster.tech/documentation-archive/v5.2.1/docker-compose
[Using JHipster in production]: https://www.jhipster.tech/documentation-archive/v5.2.1/production/
[Running tests page]: https://www.jhipster.tech/documentation-archive/v5.2.1/running-tests/
[Setting up Continuous Integration]: https://www.jhipster.tech/documentation-archive/v5.2.1/setting-up-ci/


[Node.js]: https://nodejs.org/
[Yarn]: https://yarnpkg.org/
[Webpack]: https://webpack.github.io/
[Angular CLI]: https://cli.angular.io/
[BrowserSync]: http://www.browsersync.io/
[Jest]: https://facebook.github.io/jest/
[Jasmine]: http://jasmine.github.io/2.0/introduction.html
[Protractor]: https://angular.github.io/protractor/
[Leaflet]: http://leafletjs.com/
[DefinitelyTyped]: http://definitelytyped.org/
