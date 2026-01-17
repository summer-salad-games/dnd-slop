create table if not exists spring_ai_chat_memory
(
    conversation_id varchar(36) not null,
    "content"       text        not null,
    "type"          varchar(10) not null check (type in ('USER', 'ASSISTANT', 'SYSTEM', 'TOOL')),
    "timestamp"     timestamp   not null
);

alter table spring_ai_chat_memory
    owner to dnd_slop;

create index if not exists spring_ai_chat_memory_conversation_id_timestamp_idx
    on spring_ai_chat_memory (conversation_id, "timestamp");