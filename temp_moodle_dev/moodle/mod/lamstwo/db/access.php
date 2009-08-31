<?php // $Id$
/**
 * Capability definitions for the lams module.
 *
 * For naming conventions, see lib/db/access.php.
 */
$mod_lamstwo_capabilities = array(

    'mod/lamstwo:participate' => array(

        'captype' => 'write',
        'contextlevel' => CONTEXT_MODULE,
        'legacy' => array(
            'student' => CAP_ALLOW
        )
    ),

    'mod/lamstwo:manage' => array(

        'captype' => 'write',
        'contextlevel' => CONTEXT_MODULE,
        'legacy' => array(
            'teacher' => CAP_ALLOW,
            'editingteacher' => CAP_ALLOW,
            'admin' => CAP_ALLOW
        )
    )
);
?>