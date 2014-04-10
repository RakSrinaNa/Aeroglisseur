<html>
	<head>
		<title>Resultats de la requete</title>
	</head>
	<body>
		<?php
			if(isset($_GET['vi']))
			{
		        $lines = file('variables.txt');
               	$lines[0] = 'vi='.$_GET['vi']."\n";
         		$new_content = implode('', $lines);
              	$h = fopen('variables.txt', 'w');
                fwrite($h, $new_content);
                fclose($h);
				echo '<br>';
				echo 'Vitesse modifiee a: ';
				echo $_GET['vi'];
			}
			if(isset($_GET['or']))
			{
                $lines = file('variables.txt');
                $lines[1] = 'or='.$_GET['or']."\n";
                $new_content = implode('', $lines);
                $h = fopen('variables.txt', 'w');
                fwrite($h, $new_content);
                fclose($h);
				echo '<br>';
				echo 'Orientation modifiee a: ';
				echo $_GET['or'];
			}
			if(isset($_GET['st']))
			{
                $lines = file('variables.txt');
                $lines[2] = 'st='.$_GET['st']."\n";
                $new_content = implode('', $lines);
                $h = fopen('variables.txt', 'w');
                fwrite($h, $new_content);
                fclose($h);
				echo '<br>';
				echo 'Sustentation modifiee a: ';
				echo $_GET['st'];
			}
			if(isset($_GET['cv']))
			{
				$lines = file('variables.txt');
				$lines[3] = 'cv='.$_GET['cv']."\n";
				$new_content = implode('', $lines);
				$h = fopen('variables.txt', 'w');
				fwrite($h, $new_content);
				fclose($h);
			}
			if(isset($_GET['ch']))
			{
				$lines = file('variables.txt');
				$lines[4] = 'ch='.$_GET['ch']."\n";
				$new_content = implode('', $lines);
				$h = fopen('variables.txt', 'w');
				fwrite($h, $new_content);
				fclose($h);
			}
            echo '<br><br>Current variables.txt file:<br><br>';
			foreach(file('variables.txt') as $temp)
			{
				echo $temp.'<br>';
			}
		?>
	</body>
</html>
