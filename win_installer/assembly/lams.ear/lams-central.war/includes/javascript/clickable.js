$(document).off('click').on('click', '.lams-bootpanel.panel div.clickable', function (e) {
    var $this = $(this);
    if (!$this.hasClass('panel-collapsed')) {
        $this.parent('.panel').find('.panel-body').slideUp();
        $this.addClass('panel-collapsed');
        $this.find('i.fa-minus').removeClass('fa-minus').addClass('fa-plus');
    } else {
        $this.parent('.panel').find('.panel-body').slideDown();
        $this.removeClass('panel-collapsed');
        $this.find('i.fa-plus').removeClass('fa-plus').addClass('fa-minus');
    }
});