package com.example.demo.Repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Entity.COUPONEntity;




public interface CouponRepository extends JpaRepository<COUPONEntity, Integer>{

}
