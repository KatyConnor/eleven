insertUserStmt = INSERT INTO FTP_USER (userid, userpassword,homedirectory, enableflag, writepermission, idletime, uploadrate, downloadrate) VALUES ('{userid}', '{userpassword}', '{homedirectory}', {enableflag}, {writepermission}, {idletime},{uploadrate},{downloadrate})
updateUserStmt = UPDATE FTP_USER SET userpassword='{userpassword}',homedirectory='{homedirectory}',enableflag={enableflag},writepermission={writepermission},idletime={idletime},uploadrate={uploadrate},downloadrate={downloadrate} WHERE userid='{userid}'
deleteUserStmt = DELETE FROM FTP_USER WHERE userid = '{userid}'
selectUserStmt = SELECT userid, userpassword, homedirectory,enableflag, writepermission, idletime, uploadrate, downloadrate,maxloginnumber, maxloginperip FROM FTP_USER WHERE userid = '{userid}'
selectAllStmt = SELECT userid FROM FTP_USER ORDER BY userid
isAdminStmt = SELECT userid FROM FTP_USER WHERE userid='{userid}' AND userid='admin'
authenticateStmt = SELECT userpassword from FTP_USER WHERE userid='{userid}'










