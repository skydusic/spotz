<?php
    /*$connect=mysql_connect( "localhost", "spotz", "tongood77") or die( "Unable to connect to SQL server");
    mysql_query("SET NAMES UTF-8");*/
    $username=isset($_POST['username']) ? $_POST['username'] : '';
    $listname=isset($_POST['listname']) ? $_POST['listname'] : '';
    $username = $username."/";
    $file_path = "userImageFolder/".$listname;
    chdir($file_path);

    if(is_dir($username)){
        rmdir_ok($username);
        echo "Success to Del";
    } else {
        echo "Fail to Del";
    }

    mkdir("$username", 0755, false);
    
    function rmdir_ok($dir) {
        $dirs = dir($dir); 
     while(false !== ($entry = $dirs->read())) { 
         if(($entry != '.') && ($entry != '..')) { 
             if(is_dir($dir.'/'.$entry)) { 
                   rmdir_ok($dir.'/'.$entry); 
             } else { 
                   @unlink($dir.'/'.$entry); 
             } 
         } 
     } 
     $dirs->close();
     @rmdir($dir);
    }//end of function
 ?>

