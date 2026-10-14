package com.natiuk.activityservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.natiuk.activityservice.model.Activity;

public interface ActivityRepository extends MongoRepository<Activity, String>{
}
