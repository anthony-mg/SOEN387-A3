<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:if test="${sessionScope.welcome != null}">
<script>
    $(document).ready(function(){
        $.blockUI({
            message: '\<br\>Welcome  <%=request.getSession().getAttribute("user")%> \<br\>\<br\> Now you\'re logged in \<br\>\<br\>',
            fadeIn: 700,
            fadeOut: 1000,
            timeout: 2000,
            showOverlay: false,
            centerY: false,
            css: {
                width: '400px',
                height: '200px',
                top:  ($(window).height() - 200) /2 + 'px',
                left: ($(window).width() - 400) /2 + 'px',
                border: 'none',
                padding: '5px',
                textAlign: 'center',
                font: '30px Arial,Helvetica,sans-serif',
                backgroundColor: '#000',
                '-moz-border-radius': '10px',
                '-webkit-border-radius': '10px',
                'border-radius': '10px',
                opacity: .8,
                color: '#fff'
            }
        });
    });
</script>
    <c:remove var="welcome" scope="session" />
</c:if>
<c:if test="${sessionScope.modified != null}">
    <div class="alert alert-info alert-dismissible fade show">
        <button type="button" class="close" data-dismiss="alert">&times;</button>
        <p><strong>Success!</strong> The post has been modified.</p>
    </div>
    <c:remove var="modified" scope="session" />
</c:if>
<c:if test="${sessionScope.added != null}">
    <div class="alert alert-info alert-dismissible fade show">
        <button type="button" class="close" data-dismiss="alert">&times;</button>
        <p><strong>Success!</strong> A new post has been added.</p>
    </div>
    <c:remove var="added" scope="session" />
</c:if>
<c:if test="${sessionScope.deleted != null}">
    <div class="alert alert-danger alert-dismissible fade show">
        <button type="button" class="close" data-dismiss="alert">&times;</button>
        <p>A post has been deleted</p>
    </div>
    <c:remove var="deleted" scope="session" />
</c:if>
<c:if test="${sessionScope.deletedAttachment != null}">
    <div class="alert alert-danger alert-dismissible fade show">
        <button type="button" class="close" data-dismiss="alert">&times;</button>
        <p>A post xmlAttachment has been deleted</p>
    </div>
    <c:remove var="deletedAttachment" scope="session" />
</c:if>
<c:if test="${sessionScope.emptyPosts != null}">
    <div class="alert alert-info alert-dismissible fade show">
        <button type="button" class="close" data-dismiss="alert">&times;</button>
        <p>Unfortunately there are no posts to display. Please create a new post or login with different group permissions</p>
    </div>
    <c:remove var="emptyPosts" scope="session" />
</c:if>
