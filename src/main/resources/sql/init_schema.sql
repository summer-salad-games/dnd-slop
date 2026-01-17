create schema dnd_slop authorization dnd_slop;

revoke all on schema public from public;

alter role dnd_slop set search_path = dnd_slop;

grant usage, create on schema dnd_slop to dnd_slop;

alter default privileges in schema dnd_slop
    grant select, insert, update, delete on tables to dnd_slop;

alter default privileges in schema dnd_slop
    grant usage, select on sequences to dnd_slop;

