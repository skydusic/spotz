<?php

    $connect=mysql_connect( "localhost", "spotz", "tongood77") or die( "Unable to connect to SQL server");

    mysql_query("SET NAMES UTF8");

    mysql_select_db("spotz",$connect) or die("Unable to select database");


    session_start();

    $username=isset($_POST['username']) ? $_POST['username'] : '';

    $sql = "select * from clubtable where username = '$username'";
    $result = mysql_query($sql, $connect);
    $total_record = mysql_num_rows($result);

    $sql2 = "select * from freelancer where username = '$username'";
    $result2 = mysql_query($sql2, $connect);
    $total_record2 = mysql_num_rows($result2);
    $total = $total_record + $total_record2;

    echo "{\"status\":\"OK\",\"num_results\":\"$total\",\"results\":[";

    for ($i=0; $i < $total_record; $i++)
    {
      mysql_data_seek($result, $i);
      $row = mysql_fetch_array($result);
      echo "{\"listname\":\"clubtable\",\"idx\":$row[idx],\"title\":\"$row[title]\",\"contents\":\"$row[contents]\",\"username\":\"$row[username]\",\"created\":\"$row[created]\",\"image1\":\"$row[image1]\",\"image2\":\"$row[image2]\",\"image3\":\"$row[image3]\",\"up\":\"$row[up]\",\"addedtime\":\"$row[addedtime]\"}";
    }

    if($total_record2){
      echo ",";
    }

    for ($i=0; $i < $total_record2; $i++)
    {
      mysql_data_seek($result2, $i);
      $row2 = mysql_fetch_array($result2);
      echo "{\"listname\":\"freelancer\",\"idx\":$row2[idx],\"title\":\"$row2[title]\",\"contents\":\"$row2[contents]\",\"username\":\"$row2[username]\",\"created\":\"$row2[created]\",\"image1\":\"$row2[image1]\",\"image2\":\"$row2[image2]\",\"image3\":\"$row2[image3]\",\"up\":\"$row2[up]\",\"addedtime\":\"$row2[addedtime]\"}";
    }

    echo "]}";


?>