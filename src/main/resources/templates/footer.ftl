    <footer class="py-5 px-5">
        <div class="container">
            <p>
                &copy; 2020 Google Inc
            </p>
            <p>
                <small class="text-muted">
                    This website is hosted for demo purposes only. It is not an
                    actual shop. This is not an official Google project.
                </small>
            </p>
            <small class="text-muted">
                <#if session_id??> 
                   <p> session-id: ${session_id} </p>
                </#if>
            </small>
        </div>
    </footer>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.1/js/bootstrap.min.js" integrity="sha384-smHYKdLADwkXOn1EmN1qk/HfnUcbVRZyYmZ4qpPea6sjB/pTJ0euyQp0Mk8ck+5T" crossorigin="anonymous"></script>
</body>
</html>
