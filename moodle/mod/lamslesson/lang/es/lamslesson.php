<?php

/**
 * Spanish strings for lamslesson
 *
 * You can have a rather longer description of the file as well,
 * if you like, and it can span multiple lines.
 *
 * @package   mod_lamslesson
 * @copyright 2011 LAMS Foundation - Ernie Ghiglione (ernieg@lamsfoundation.org) 
 * @license  http://www.gnu.org/licenses/gpl-2.0.html GNU GPL v2
 */

defined('MOODLE_INTERNAL') || die();

$string["modulename"] = "LAMS Lesson";
$string["modulenameplural"] = "LAMS Lessons";
$string["modulename_help"] = "El módulo de lección LAMS permite a los profesores crear lecciones LAMS dentro de Moodle.

LAMS proporciona a los profesores un entorno de creación visual intuitivo para crear secuencias de actividades de aprendizaje. Estas actividades pueden incluir una variedad de tareas individuales, trabajo en grupos pequeños y actividades de toda la clase basadas tanto en contenido como en colaboración.

Una vez que se crea una secuencia, se puede reutilizar en uno o varios cursos.

Además, LAMS proporciona una interfaz de seguimiento y seguimiento en tiempo real donde los maestros pueden interactuar con los estudiantes a medida que avanzan en las actividades de aprendizaje.

Para obtener más información, visite: lamsfoundation.org.";
$string["modulename_link"] = "lamslesson";
$string["lamslessonfieldset"] = "Conjunto de campos de ejemplo personalizado";
$string["lamslessonname"] = "Nombre de lección";
$string["lamslessonname_help"] = "Nombre que desea darle a la lección";
$string["lamslesson"] = "Lección LAMS";
$string["pluginadministration"] = "Administración de Lecciones LAMS";
$string["pluginname"] = "Lecciones LAMS";
$string["selectsequence"] = "Seleccionar diseño";
$string["displaydesign"] = "¿Mostrar imagen del diseño?";
$string["displaydesign_help"] = "Si está habilitado, cuando la lección se muestre a los estudiantes, mostrará el diagrama de diseño de aprendizaje.";
$string["allowlearnerrestart"] = "Learners can restart the lesson?";
$string["allowlearnerrestart_help"] = "Si está habilitado, los estudiantes podrán reiniciar la lección y comenzar desde el principio en cualquier momento. En cada reinicio, se elimina el progreso anterior.";
$string["availablesequences"] = "Diseños";
$string["openauthor"] = "Crear nuevos diseños";
$string["refresh"] = "Actualizar";
$string["lamslesson:manage"] = "Administrar lecciones";
$string["lamslesson:participate"] = "Participar de lecciones";
$string["adminheader"] = "Configuración del servidor LAMS";
$string["admindescription"] = "Configure los detalles de su servidor LAMS. Asegúrese de que los valores proporcionados sean los mismos que han sido configurados en el servidor LAMS. De otra manera, la integración no funcionará.";
$string["serverurl"] = "URL servidor LAMS:";
$string["serverurlinfo"] = "Aqui debe ingresar la URL del servidor LAMS ie: http://lams.com:8080/lams/.";
$string["serverid"] = "¿Server ID:";
$string["serveridinfo"] = "Cuál es el Server ID que ha configurado en LAMS?";
$string["serverkey"] = "Server Key:";
$string["serverkeyinfo"] = "¿Cuál es el Server Key que ha configurado en?";
$string["validationbutton"] = "Validar configuración";
$string["validationheader"] = "Validación de configuración";
$string["validationinfo"] = "Antes de guardar sus configuraciones, presione el botón para validarlas con el servidor LAMS. Si la validación es correcta, guarde esta configuración. En caso contrario, compruebe que la configuración que ha introducido coincida con los valores del servidor LAMS.";
$string["validationhelp"] = "Necesita ayuda";
$string["offsetbutton"] = "Calcular diferencia horaria";
$string["offsetinfo"] = "Si está aplicando el límite de tiempo de vida de la solicitud de inicio de sesión, es importante que configure la diferencia de tiempo (en minutos) entre el LAMS y su servidor Moodle. Haga clic en Calcular compensación para ver si hay una diferencia de tiempo entre su servidor LAMS y Moodle. Tome el tiempo de compensación que se muestra y agréguelo al ajuste \"Diferencia de tiempo de compensación\".";
$string["servertimeoffset"] = "Diferencia horaria (minutos)";
$string["servertimeoffsetinfo"] = "Esta es la diferencia horaria (o compensación de tiempo) entre el servidor LAMS y Moodle.";
$string["offsetheader"] = "Diferencia horaria";
$string["lamsmoodlehelp"] = "LAMS-Moodle integration tutorial";
$string["validationsuccessful"] = "¡Validación exitosa! Ahora puede guardar su configuración y comenzar a usar LAMS dentro de Moodle.";
$string["validationfailed"] = "Error de validación: compruebe que la configuración que ha introducido coincida con la configuración de LAMS.";
$string["restcallfail"] = "La comunicación con LAMS ha fallado: no se recibió respuesta o se rechazó la conexión. Compruebe que tiene la URL correcta del servidor LAMS y que está en línea.";
$string["sequencenotselected"] = "Debe seleccionar un diseño antes de continuar.";
$string["previewthislesson"] = "Vista previa";
$string["updatewarning"] = "Atención: seleccionar un diseño distinto al actualmente seleccionado creará una nueva lección para los estudiantes. Esto puede resultar en algunos estudiantes un poco confundidos";
$string["currentsequence"] = "Diseño actual:";
$string["nolessons"] = "No hay lecciones hasta ahora.";
$string["lessonname"] = "Lección";
$string["links"] = "Enlaces";
$string["introduction"] = "Introducción";
$string["openmonitor"] = "Seguimiento de lección";
$string["lastmodified"] = "Última modificación";
$string["openlesson"] = "Abrir lección";
$string["empty"] = "vacio";
$string["completionfinish"] = "Mostrar como completo cuando el usuario terminó la lección";
$string["yourprogress"] = "Progreso";
$string["youhavecompleted"] = "Ha completado: ";
$string["outof"] = "de aproximadamente";
$string["lessonincompleted"] = "La lección no ha sido completada";
$string["lessoncompleted"] = "Ha completado esta lección";
$string["activities"] = "actividades";
$string["ymmv"] = "Las actividades totales dependen de su ruta de aprendizaje.";
$string["yourmarkis"] = "Su puntaje final es:";
$string["outofmark"] = "de";
