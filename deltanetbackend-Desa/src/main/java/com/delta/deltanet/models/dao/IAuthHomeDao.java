package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.AuthHome;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAuthHomeDao extends JpaRepository<AuthHome,Long> {
}
