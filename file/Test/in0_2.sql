use Sky;
select Galaxy.objID, Galaxy.parentID, Galaxy.r, Star.objID, Star.parentID, Star.r from Galaxy, Star where Galaxy.parentID > 0 and Galaxy.parentID = Star.parentID;