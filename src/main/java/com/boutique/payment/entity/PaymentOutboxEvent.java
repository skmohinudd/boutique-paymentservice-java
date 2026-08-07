package com.boutique.payment.entity;
import jakarta.persistence.*;import java.time.Instant;import java.util.UUID;
@Entity @Table(name="payment_outbox_events")
public class PaymentOutboxEvent{
 @Id private UUID id;@Column(name="aggregate_id",nullable=false)private UUID aggregateId;@Column(name="event_type",nullable=false)private String eventType;
 @Column(nullable=false,columnDefinition="text")private String payload;@Column(name="created_at",nullable=false)private Instant createdAt;
 @Column(name="kafka_published_at")private Instant kafkaPublishedAt;@Column(name="rabbit_published_at")private Instant rabbitPublishedAt;
 protected PaymentOutboxEvent(){} public PaymentOutboxEvent(UUID a,String t,String p){id=UUID.randomUUID();aggregateId=a;eventType=t;payload=p;createdAt=Instant.now();}
 public UUID getId(){return id;}public UUID getAggregateId(){return aggregateId;}public String getEventType(){return eventType;}public String getPayload(){return payload;}
 public Instant getKafkaPublishedAt(){return kafkaPublishedAt;}public Instant getRabbitPublishedAt(){return rabbitPublishedAt;}
 public void kafkaPublished(){kafkaPublishedAt=Instant.now();}public void rabbitPublished(){rabbitPublishedAt=Instant.now();}
}
