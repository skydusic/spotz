<?php

    $connect=mysql_connect( "localhost", "spotz", "tongood77") or die( "Unable to connect to SQL server");

    mysql_query("SET NAMES UTF8");

    mysql_select_db("spotz",$connect) or die("Unable to select database");

    session_start();

    $sql ="select * from noticetable order by created desc";

    $result = mysql_query($sql, $connect);

    $total_record = mysql_num_rows($result);

    echo "{\"status\":\"OK\",\"num_results\":\"$total_record\",\"results\":[";

    for ($i=0; $i < $total_record; $i++)
    {
      mysql_data_seek($result, $i);
      $row = mysql_fetch_array($result);
      echo "{\"idx\":$row[idx],\"title\":\"$row[title]\",\"contents\":\"$row[contents]\",\"image\":\"$row[image]\",\"created\":\"$row[created]\"}";

    if($i<$total_record-1){
      echo ",";
    }

    }

    echo "]}";


?>