# spring-mongodb
Demo project to illustrate Spring data mongodb features

# MONGODB: 
To know more about mongodb in a stand-alone manner, visit the official docs:
[Mongodb home](https://www.mongodb.com/docs/manual/)
## Mongo Repository

Just like Spring Data JPA provides abstraction over low-level relational schema data persistence,
Spring Data MongoDB provides abstraction over low-level document-based nosql data persistence.

Mongo repository is the abstraction layer provided by Spring Data MongoDB Starter dependency

At the core of this dependency , there lies the MongoRepository interface.
It extends the CrudRepository and PagingAndSortingRepository in its hierarchy and therefore lets the
client use the well-known crud operations along with paging and sorting.

It also allows the user to write  custom queries based on naming conventions by leveraging derived queries feature.

If nuanced, custom operations needed, standard Mongodb queries can be written using MongoDB operators using the
@Query annotation .

[Comprehensive list of MongoDB operators](https://www.mafiree.com/readBlog/top-10-mongodb-operators-every-developer-should-know-with-examples)


