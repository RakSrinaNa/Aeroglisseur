<html>
	<head>
		<title>Resultats de la requete</title>
	</head>
	<body>
		<?php
			if(isset($_GET['vi']))
			{
		                $lines = file('test.txt');
               			$lines[0] = 'vi='.$_GET['vi']."\n";
         			$new_content = implode('', $lines);
              			$h = fopen('test.txt', 'w');
                		fwrite($h, $new_content);
                		fclose($h);
				echo '<br>';
				echo 'Vitesse modifiee a: ';
				echo $_GET['vi'];
			}
			if(isset($_GET['or']))
			{
                		$lines = file('test.txt');
                		$lines[1] = 'or='.$_GET['or']."\n";
                		$new_content = implode('', $lines);
                		$h = fopen('test.txt', 'w');
                		fwrite($h, $new_content);
                		fclose($h);
				echo '<br>';
				echo 'Orientation modifiee a: ';
				echo $_GET['or'];
			}
			if(isset($_GET['st']))
			{
                		$lines = file('test.txt');
                		$lines[2] = 'st='.$_GET['st']."\n";
                		$new_content = implode('', $lines);
                		$h = fopen('test.txt', 'w');
                		fwrite($h, $new_content);
                		fclose($h);
				echo '<br>';
				echo 'Sustentation modifiee a: ';
				echo $_GET['st'];
			}
		?>
	</body>
</html>
