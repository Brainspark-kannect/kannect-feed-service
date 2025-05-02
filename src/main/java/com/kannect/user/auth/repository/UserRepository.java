package com.kannect.user.auth.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.kannect.user.auth.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByUserName(String userName);

	List<User> findByActive(Boolean active);

	@Query("SELECT u.id FROM User u")
	List<Long> findAllUserIds();

}
