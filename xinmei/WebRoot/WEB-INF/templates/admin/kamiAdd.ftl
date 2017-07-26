<div class="bjui-pageContent">
    <form action="kamiSave" method="post" class="pageForm" data-toggle="validate" data-reload-navtab="true">
        <div class="pageFormContent" data-layout-h="0">
            <table class="table table-condensed table-hover">
                <thead>
                    <tr>
                    	<td colspan="2" align="center"><h3>添加</h3></td>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>
                            <label for="name" class="control-label x150">所属产品：</label>
                            ${product.title}
                            <input type="hidden" name="pid" value="${product.id}"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label for="name" class="control-label x150">分销码信息：</label>
                            <textarea rows="5" cols="50" name="infos"></textarea>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label for="name" class="control-label x150">说明：</label>
                            	多个分销码之间用换行，分销码格式：卡号:密码
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
        <div class="bjui-footBar">
            <ul>
                <li><button type="button" class="btn-close">关闭</button></li>
                <li><button type="submit" class="btn-default">保存</button></li>
            </ul>
        </div>
    </form>
</div>