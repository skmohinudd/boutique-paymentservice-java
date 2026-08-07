package com.boutique.payment.repository;
import com.boutique.payment.entity.PaymentOutboxEvent;import org.springframework.data.jpa.repository.*;import org.springframework.data.repository.query.Param;import java.util.*;
public interface PaymentOutboxRepository extends JpaRepository<PaymentOutboxEvent,UUID>{
 @Query(value="select * from payment_outbox_events where kafka_published_at is null or rabbit_published_at is null order by created_at limit :limit for update skip locked",nativeQuery=true)
 List<PaymentOutboxEvent> lockBatch(@Param("limit")int limit);
}
