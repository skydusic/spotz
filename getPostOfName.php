<?php

    $connect=mysqli_connect( "localhost", "spotz", "tongood77") or die( "Unable to connect to SQL server");

    mysqli_set_charset($connect,"utf8");  
    mysqli_select_db($connect,"spotz") or die("Unable to select database");
    session_start();

    $username=isset($_POST['username']) ? $_POST['username'] : '';

    $sql = "select * from freelancer where username = '$username'";
    $result = mysqli_query($connect,$sql);
    $total_record = mysqli_num_rows($result);

    $sql2 = "select * from clubtable where username = '$username'";
    $result2 = mysqli_query($connect,$sql2);
    $total_record2 = mysqli_num_rows($result2);

    $total = $total_record + $total_record2;

    echo "{\"status\":\"OK\",\"num_results\":\"$total\",\"results\":[";

    for ($i=0; $i < $total_record; $i++)
        {
            mysqli_data_seek($result, $i);
            $row = mysqli_fetch_array($result);
            echo "{\"listname\":\"freelancer\",\"idx\":$row[idx],\"title\":\"$row[title]\",\"contents\":\"$row[contents]\",\"username\":\"$row[username]\",\"created\":\"$row[created]\",\"image\":\"$row[image]\",\"up\":\"$row[up]\",\"addedtime\":\"$row[addedtime]\",\"hit\":\"$row[hit]\",\"postidx\":\"\",\"text1\":\"\",\"text2\":\"\",\"text3\":\"\",\"text4\":\"\",\"text5\":\"\"}";

            if($i<$total_record){
                echo ",";
            }
        }

    for ($i=0; $i < $total_record2; $i++)
    {
      mysqli_data_seek($result2, $i);
      $row2 = mysqli_fetch_array($result2);
      $sqlEx = "select * from clubextension where postidx = '$row2[idx]'";
      $resultex = mysqli_query($connect,$sqlEx);
      mysqli_data_seek($resultex, 0);
      $rowex = mysqli_fetch_array($resultex);
      echo "{\"listname\":\"clubtable\",\"idx\":\"$row2[idx]\",\"postidx\":\"$row2[postidx]\",\"contents\":\"$row2[contents]\",\"username\":\"$row2[username]\",\"created\":\"$row2[created]\",\"image\":\"$row2[image]\",\"hit\":\"$row2[hit]\",\"postidx\":\"$rowex[postidx]\",\"text1\":\"$rowex[corperation]\",\"text2\":\"$rowex[sports]\",\"text3\":\"$rowex[location]\",\"text4\":\"$rowex[phone]\",\"text5\":\"$rowex[etc]\"}";
        
        if($i<$total_record2-1){
            echo ",";
        }
    }

    echo "]}";
    mysqli_close($connect);
?>