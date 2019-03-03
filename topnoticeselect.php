<?php

    $connect=mysqli_connect( "localhost", "spotz", "tongood77") or die( "Unable to connect to SQL server");
    if (!$connect) {
        $error = mysqli_connect_error();
        $errno = mysqli_connect_errno(); 
        die("Connection failed : "."$errno : $error\n");
        exit(); 
    }
    
    mysqli_set_charset($connect,"utf8");  
    mysqli_select_db($connect,"spotz") or die("Unable to select database");
    session_start();

    $sql ="select * from topnotice order by created desc";

    $result = mysqli_query($connect,$sql);

    $total_record = mysqli_num_rows($result);

    echo "{\"status\":\"OK\",\"num_results\":\"$total_record\",\"results\":[";

    for ($i=0; $i < $total_record; $i++)
    {
      mysqli_data_seek($result, $i);
      $row = mysqli_fetch_array($result);
      echo "{\"idx\":$row[idx],\"title\":\"$row[title]\",\"contents\":\"$row[contents]\",\"image\":\"$row[image]\",\"created\":\"$row[created]\",\"hit\":\"$row[hit]\"}";

    if($i<$total_record-1){
      echo ",";
    }

    }

    echo "]}";

    mysqli_close($connect);
?>