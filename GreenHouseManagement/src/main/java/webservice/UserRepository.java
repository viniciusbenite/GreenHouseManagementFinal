package webservice;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

interface CustomerRepository extends MongoRepository<Users, String> {

    public String findByMail(String user);

}