<!DOCTYPE html>
<html>
    <head>
        <title>任务管理中心</title>

        <link rel="stylesheet" href="/css/bootstrap.min.css" >
        <link rel="stylesheet" href="/css/bootstrap-theme.min.css" >
        <link rel="stylesheet" href="/css/index.css" >
        <link rel="stylesheet" href="/css/bootstrapValidator.min.css">
        <link rel="stylesheet" href="/css/jquery-confirm.min.css">
        <script src="/js/jquery-3.2.1.min.js"></script>
        <script src="/js/jquery-confirm.min.js"></script>
        <script src="/js/bootstrap.min.js" ></script>
        <script src="/js/bootstrapValidator.min.js"></script>
        <script src="/js/index.js" ></script>
    </head>
    <body>
    <div class="row">
        <div class="col-sm-9">
            <div class="ibox float-e-margins">
                <div class="ibox-title">
                    <button class="btn btn-sm btn-info pull-right m-t-n-xs" id="addJobBtn"><i class="fa fa-refresh"></i> 新增</button>
                    <button class="btn btn-sm btn-info pull-right m-t-n-xs" id="delJobBtn"><i class="fa fa-refresh"></i> 删除</button>
                </div>
                <div class="ibox-content">
                    <table class="table table-striped table-bordered table-hover">
                        <thead>
                            <th></th>
                            <th>JobName</th>
                            <th>BeanName</th>
                            <th>Cron</th>
                        </thead>
                        <tbody id="job_body">
                            <#list datas as data>
                                <tr>
                                    <td><input type="checkbox" name="job" value="${data.jobName}"/></td>
                                    <td>${data.jobName}</td>
                                    <td>${data.beanName}</td>
                                    <td>${data.cron}</td>
                                </tr>
                            </#list>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <div class="modal fade" id="addJob_window" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h4 class="modal-title" id="myModalLabel">新增定时任务</h4>
                    </div>
                    <form id="importFileForm" class="form-horizontal" method="post">
                        <div class="modal-body">
                            <fieldset>
                                <legend>Not Empty validator</legend>
                                <div class="form-group">
                                    <label class="col-lg-3 control-label">JobName</label>
                                    <div class="col-lg-5">
                                        <input type="text" class="form-control" name="jobName"  id="jobName"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-lg-3 control-label">BeanName</label>
                                    <div class="col-lg-5">
                                        <input type="text" class="form-control" name="beanName"  id="beanName"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-lg-3 control-label">Cron</label>
                                    <div class="col-lg-5">
                                        <input type="text" class="form-control" name="cron"  id="cron"/>
                                    </div>
                                </div>
                            </fieldset>
                        </div>
                        <div class="modal-footer">
                            <input type="button" id="saveBtn" class="btn btn-primary" value="保存" />
                        </div>
                    </form>
                </div>
                <!-- /.modal-content -->
            </div>
        </div>
    </body>
</html>
