    $(document).ready(function () {
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
});

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
                    showToast("❌ Something went wrong!", "error");
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

