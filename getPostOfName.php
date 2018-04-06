<?php

    $connect=mysqli_connect( "localhost", "spotz", "tongood77") or die( "Unable to connect to SQL server");

    mysqli_set_charset($connect,"utf8");  
    mysqli_select_db($connect,"spotz") or die("Unable to select database");
    session_start();

    $username=isset($_POST['username']) ? $_POST['username'] : '';

    $sql = "select * from clubtable where username = '$username'";
    $result = mysqli_query($connect,$sql);
    $total_record = mysqli_num_rows($result);

    $sqlEx = "select * from clubextension where postidx = '$row[idx]'";
    $resultex = mysqli_query($connect,$sqlEx);
    mysqli_data_seek($resultex, 0);
    $row2 = mysqli_fetch_array($resultex);

    $sql2 = "select * from freelancer where username = '$username'";
    $result2 = mysqli_query($connect,$sql2);
    $total_record2 = mysqli_num_rows($result2);
    $total = $total_record + $total_record2;

    echo "{\"status\":\"OK\",\"num_results\":\"$total\",\"results\":[";

    for ($i=0; $i < $total_record; $i++)
    {
      mysqli_data_seek($result, $i);
      $row = mysqli_fetch_array($result);
      echo "{\"listname\":\"clubtable\",\"idx\":$row[idx],\"contents\":\"$row[contents]\",\"username\":\"$row[username]\",\"created\":\"$row[created]\",\"hit\":\"$row[hit]\",\"image\":\"$row[image]\",\"corperation\":\"$row2[corperation]\",\"sports\":\"$row2[sports]\",\"location\":\"$row2[location]\",\"phone\":\"$row2[phone]\",\"etc\":\"$row2[etc]\"";
    }

    if($total_record2){
      echo ",";
    }

    for ($i=0; $i < $total_record2; $i++)
    {
      mysqli_data_seek($result2, $i);
      $row2 = mysqli_fetch_array($result2);
      echo "{\"listname\":\"freelancer\",\"idx\":$row2[idx],\"title\":\"$row2[title]\",\"contents\":\"$row2[contents]\",\"username\":\"$row2[username]\",\"created\":\"$row2[created]\",\"image\":\"$row2[image]\",\"up\":\"$row2[up]\",\"addedtime\":\"$row2[addedtime]\",\"hit\":\"$row2[hit]\"}";
    }

    echo "]}";
    mysqli_close($connect);
?>