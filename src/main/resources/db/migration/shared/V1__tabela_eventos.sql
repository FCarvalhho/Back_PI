/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  Cansei2
 * Created: 30 de abr. de 2026
 */

CREATE TABLE IF NOT EXISTS event_publication (
    id UUID NOT NULL,
    completion_attempts INT4,
    completion_date TIMESTAMP(6) WITH TIME ZONE,
    event_type VARCHAR(255),
    last_resubmission_date TIMESTAMP(6) WITH TIME ZONE,
    listener_id VARCHAR(255),
    publication_date TIMESTAMP(6) WITH TIME ZONE,
    serialized_event VARCHAR(255),
    status VARCHAR(255),
    PRIMARY KEY (id)
);