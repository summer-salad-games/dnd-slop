create role dnd_slop
    login
    password 'dnd_slop'
    nosuperuser
    nocreatedb
    nocreaterole
    noinherit;

create database dnd_slop owner dnd_slop encoding 'UTF8';

alter database dnd_slop set search_path = dnd_slop;
