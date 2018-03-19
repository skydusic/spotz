<?php

    $connect=mysql_connect( "localhost", "spotz", "tongood77") or die( "Unable to connect to SQL server");

    mysql_query("SET NAMES UTF8");

    mysql_select_db("spotz",$connect) or die("Unable to select database");


    session_start();

    $idx=isset($_POST['idx']) ? $_POST['idx'] : '';

    if($idx == ''){
        $sql = "select * from freelancer";
    } else {
        $sql = "select * from freelancer where idx = '$idx'";
    }

    
    
    $result = mysql_query($sql, $connect);

    $total_record = mysql_num_rows($result);

    echo "{\"status\":\"OK\",\"num_results\":\"$total_record\",\"results\":[";

    for ($i=0; $i < $total_record; $i++)
    {
      mysql_data_seek($result, $i);
      $row = mysql_fetch_array($result);
      echo "{\"idx\":$row[idx],\"title\":\"$row[title]\",\"contents\":\"$row[contents]\",\"username\":\"$row[username]\",\"created\":\"$row[created]\",\"image1\":\"$row[image1]\",\"image2\":\"$row[image2]\",\"image3\":\"$row[image3]\"}";

    if($i<$total_record-1){
      echo ",";
    }

    }

    echo "]}";


?>