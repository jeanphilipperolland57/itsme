package com.bnp.pf.transformation.identifyme.proxy.repository;

import com.bnp.pf.transformation.identifyme.proxy.entity.SecUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<SecUser, String> {}
