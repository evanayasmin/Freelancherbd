$(document).ready(function() {

     $('#editModal').on('show.bs.modal', function (e) {
        var button = $(e.relatedTarget);     // Button that triggered the modal
        var url = button.data('url');        // Extract url from data-url

        // Clear old content before loading new
        $('#editModalContent').html('<p class="text-center">Loading...</p>');
        // Load form via AJAX
        $.ajax({
            url: url,
            type: "GET",
            success: function (data) {
              $('#editModalContent').html(data);
            },
            error: function () {
                $('#editModalContent').html('<p class="text-danger">Failed to load form.</p>');
            }
        });
    });

    // UserDetails Modal Display On click view button.
    $('#viewModal').on('show.bs.modal', function (e) {
        var button = $(e.relatedTarget);
        var url = button.data('url');
        // Clear old content before loading new
        $('#viewModalContent').html('<p class="text-center">Loading...</p>');
        $.ajax({
            url: url,
            type: "GET",
            success: function (data) {
                $('#viewModalContent').html(data);
            },
            error: function () {
                $('#viewModalContent').html('<p class="text-danger">Failed to load form.</p>');
            }
        });
    });

    //JobDetails Modal Display On click view button.
    $('#jobViewModal').on('show.bs.modal', function (e) {
        var button = $(e.relatedTarget);
        var url = button.data('url');

        // Clear old content before loading new
        $('#viewModalContent').html('<p class="text-center">Loading...</p>');
        $.ajax({
            url: url,
            type: "GET",
            success: function (data) {
                $('#viewModalContent').html(data);
                tinymce.remove();
                tinymce.init({ selector: 'textarea.tinymce-editor' });
            },
            error: function () {
                $('#viewModalContent').html('<p class="text-danger">Failed to load form.</p>');
            }
        });
    });
    //Closing the modal
    $('#jobViewModal').on('hidden.bs.modal', function () {
        tinymce.remove();
    });
});

//User Status Updating:
$(document).on("submit", "#statusUpdateForm", function(e) {
    e.preventDefault();
    console.log("AJAX triggered from dynamic modal");
    const userId = $("#encId").val();
    const status = $("#statusSelect").val();
    $.ajax({
        url: "/admin/users/statusUpdate",
        type: "POST",
        data: { encId: userId, status: status },
        success: function() {
            $("#statusMessage").text("Status updated successfully!").show();
            //$("#main-content").load(" #main-content");
        },
        error: function() {
            $("#statusMessage").text("Error occurred!").show();
        }
    });
});

//Job Detail Updating Submit:
$(document).on("submit", "#jobUpdateForm", function(e) {
    e.preventDefault();
    console.log("AJAX triggered from dynamic modal");
    $.ajax({
        url: $(this).attr('action'),
        type: "POST",
        data: $(this).serialize(),
        success: function(res) {
           // $("#statusMessage").text("Job updated successfully!").show();
            if (res.status === "success") {
                showToast("Job updated successfully!", "success");
            }else{
                showToast("Failed to update job!", "error");
            }

        },
        error: function() {
            showToast("Bad Request!", "error");
        }
    });
});

// Toast function for global
function showToast(message, type) {
    let bg = type === "success" ? "bg-success" : "bg-danger";
    let toastHtml = `
            <div class="toast align-items-center text-white ${bg} border-0" role="alert" aria-live="assertive" aria-atomic="true">
              <div class="d-flex">
                <div class="toast-body">${message}</div>
                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>
              </div>
            </div>`;
    $("#toastContainer").append(toastHtml);
    let newToast = new bootstrap.Toast($("#toastContainer .toast").last()[0]);
    newToast.show();
}

//Category Update Form
$(document).ready(function () {
    // Handle form submit inside modal
    $(document).on('submit', '#categoryUpdateForm', function (e) {
            e.preventDefault();
            $.ajax({
                url: $(this).attr('action'),
                type: "POST",
                data: $(this).serialize(),
                success: function (res) {
                    if (res.status === "success") {
                       // $('#editModal').modal('hide');  // auto close modal
                        var modalEl = document.getElementById('editModal');
                        var modal = bootstrap.Modal.getInstance(modalEl) || new bootstrap.Modal(modalEl);
                        modal.hide();
                        showToast(res.message, "success");
                        // Optionally refresh category list here (AJAX reload table)
                    } else {
                        showToast(res.message, "error");
                    }
                },
                error: function () {
                    showToast("Something went wrong!", "error");
                }
            });
        });

        // When a delete button is clicked, set the URL in the modal's confirm button
        $('.deleteBtn').on('click', function () {
            let url = $(this).data('url');
            $('#confirmDeleteBtn').attr('href', url);
        });

        // When confirm delete button in modal is clicked
        $('#confirmDeleteBtn').on('click', function (e) {
            e.preventDefault();
            let url = $(this).attr('href');
            //alert(url);
            $.ajax({
                url: url,
                type: "GET",
                success: function (response) {
                    $('#deleteModal').modal('hide');  // close modal
                    showToast(response, "success");
                    $('#table1').DataTable().ajax.reload(null, false); // refresh table
                },
                error: function () {
                    alert("Error deleting category");
                }
            });
        });

    // Toast function
    function showToast(message, type) {
        let bg = type === "success" ? "bg-success" : "bg-danger";
        let toastHtml = `
            <div class="toast align-items-center text-white ${bg} border-0" role="alert" aria-live="assertive" aria-atomic="true">
              <div class="d-flex">
                <div class="toast-body">${message}</div>
                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>
              </div>
            </div>`;
        $("#toastContainer").append(toastHtml);
        let newToast = new bootstrap.Toast($("#toastContainer .toast").last()[0]);
        newToast.show();
    }
});


