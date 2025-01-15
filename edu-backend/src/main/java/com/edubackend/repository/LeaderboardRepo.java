package com.edubackend.repository;

import com.edubackend.model.Leaderboard;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface LeaderboardRepo extends MongoRepository<Leaderboard, String>{
}
