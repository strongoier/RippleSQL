use Sky;
select Galaxy.objID, Galaxy.parentID, Galaxy.r from Galaxy where Galaxy.parentID > 0;