$(function(){

    $("#addJobBtn").click(function(){
        $('#addJob_window').modal('show');
    });

    $('#importFileForm').bootstrapValidator({
        message: 'This value is not valid',
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            jobName: {
                message: 'jobName不能为空',
                validators: {
                    notEmpty: {
                        message: 'jobName不能为空'
                    }
                }
            },
            beanName: {
                validators: {
                    notEmpty: {
                        message: 'beanName不能为空'
                    }
                }
            },
            cron: {
                validators: {
                    notEmpty: {
                        message: 'cron不能为空'
                    }
                }
            }
        }
    });
    $("#saveBtn").click(function(){
        $('#importFileForm').bootstrapValidator('validate');
        if($("#importFileForm").data("bootstrapValidator").isValid()) {
            $.ajax({
                url: '/config/add',
                data: {
                    jobName: $("#jobName").val(),
                    beanName: $("#beanName").val(),
                    cron:$("#cron").val()
                },
                success:function(res){
                    if(res.code===200){
                        $('#addJob_window').modal('hide');
                        $("#job_body").append("<tr>" +
                            "<td><input type='checkbox' name='job' value='"+$("#jobName").val()+"'/></td>"+
                            "<td>" + $("#jobName").val()+"</td>"+
                            "<td>" + $("#beanName").val()+"</td>"+
                            "<td>" + $("#cron").val()+"</td>"+
                            "</tr>");
                    }
                }
            });
        }
    });

    $("#delJobBtn").click(function(){
        $.confirm({
            title: '请确认!',
            content: '任务删除后将无法恢复，你确认要删除当前所选任务!',
            buttons: {
                done: {
                    text: '确认',
                    btnClass: 'btn-green',
                    action: function () {
                        $("input[name='job']").each(
                            function(){
                                if($(this).is(':checked')){
                                    var el = this;
                                    $.ajax({
                                        url: '/config/del',
                                        data: {
                                            jobName: $(this).val()
                                        },
                                        success:function(res){
                                            if(res.code===200){
                                                $(el).parents("tr").remove();
                                            }
                                        }
                                    });
                                }
                            }
                        );
                    }
                },
                cancel:{
                    text: '取消',
                    btnClass: 'btn-yellow',
                    action: function () {

                    }
                }
            }
        });
    })
})