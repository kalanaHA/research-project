<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Patient Risk Advisor</title>

    <link rel="preconnect" href="https://fonts.gstatic.com">
    <link href="https://fonts.googleapis.com/css2?family=Nunito:wght@300;400;600;700;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="{{ asset('assets/css/bootstrap.css') }}">

    <link rel="stylesheet" href="{{ asset('assets/vendors/apexcharts/apexcharts.css') }}">

    <style>
        .form-floating>.bi-calendar-date+.datepicker_input+label {
            padding-left: 3.5rem;
            z-index: 3;
        }
    </style>
    <link rel="stylesheet" href="{{ asset('assets/css/app.css') }}">
    <link rel="shortcut icon" href="{{ asset('assets/images/favicon.svg') }}" type="image/x-icon">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.5.0/font/bootstrap-icons.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/vanillajs-datepicker@1.1.4/dist/css/datepicker.min.css">
</head>

<body>
    <div id="main" class="ms-0 container-fluid">
        {{-- <header class="mb-3">
            <a href="#" class="burger-btn d-block d-xl-none">
                <i class="bi bi-justify fs-3"></i>
            </a>
        </header> --}}

        <div class="page-heading">
            <div class="page-title">
                <div class="row">
                    <div class="col-12 col-md-6 order-md-1 order-last">
                        <h3>Risk Advisor</h3>
                        <p class="text-subtitle text-muted">Patient Report </p>
                    </div>
                    <div class="col-12 col-md-6 order-md-2 order-first">
                        <nav aria-label="breadcrumb" class="breadcrumb-header float-start float-lg-end">
                            <ol class="breadcrumb">
                                {{-- <li class="breadcrumb-item"><a href="index.html">Dashboard</a></li>
                                <li class="breadcrumb-item active" aria-current="page">Patient Charts</li> --}}
                            </ol>
                        </nav>
                        {{-- <button class="btn btn-outline-primary" onclick="printPDF()">Print PDF</button> --}}
                        <button class="btn btn-outline-primary"
                            onclick="this.style.display = 'none'; document.getElementById('filter').style.display = 'none' ; window.print();">Print
                            PDF</button>
                    </div>
                </div>
            </div>

            <section class="section">
                <div class="row justify-content-center">
                    <div class="col-7">

                    </div>
                    <div class="col">

                        <div class="row align-items-center">

                            <div class="col">

                                <div class="form-group mb-4">
                                    <label for="datepicker1">From : </label>
                                    <input type="text" class="text-sm text-center datepicker_input form-control"
                                        id="datepicker1" required value="{{ now()->format('d-m-Y') }}">
                                </div>
                            </div>
                            <div class="col">

                                <div class="form-group mb-4">
                                    <label for="datepicker1">To : </label>
                                    <input type="text" class="text-sm text-center datepicker_input form-control"
                                        id="datepicker2" required value="{{ now()->format('d-m-Y') }}">
                                </div>
                            </div>
                            <div class="col">
                                <div class="filter">
                                    <button class="btn btn-success btn-sm px-2" id="filter"
                                        onclick="getCharts()">Filter</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="row mb-4">
                    <h5>Patient Details</h5>
                    <div class="details">

                        @php
                            use Carbon\Carbon;
                            // dd($user);
                            $age = now()->year - Carbon::parse($user->dob)->year;
                        @endphp
                        <p class="ms-3 mb-0"> <b>Name :</b> {{ $user->name }}</p>
                        <p class="ms-3 mb-0"> <b>Age :</b> {{ $age }} years</p>
                        <p class="ms-3 mb-0"> <b>Blood Group :</b>
                            {{ $user->blood_group[0] }}<sup>{{ $user->blood_group[1] }}</sup></p>
                        <p class="ms-3 mb-0"> <b>Health Status :</b> {{ $user->health_status }}</p>
                        <p class="ms-3 mb-0"> <b>Address :</b> {{ $user->address }}</p>
                    </div>
                </div>

                <div class="row justify-content-center">
                    <div class="col-md-9">
                        <div class="card">
                            <div class="card-header">
                                <h4>Body Temperature</h4>
                            </div>
                            <div class="card-body">
                                <div id="area1"></div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row justify-content-center">
                    <div class="col-md-9">
                        <div class="card">
                            <div class="card-header">
                                <h4>Blood Oxygen Level</h4>
                            </div>
                            <div class="card-body">
                                <div id="area2"></div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row justify-content-center">
                    <div class="col-md-9">
                        <div class="card">
                            <div class="card-header">
                                <h4>HeartRate</h4>
                            </div>
                            <div class="card-body">
                                <div id="area3"></div>
                            </div>
                        </div>
                    </div>
                </div>
            </section>
        </div>
    </div>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/html2pdf.js/0.9.3/html2pdf.bundle.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.1.1/js/bootstrap.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/vanillajs-datepicker@1.1.4/dist/js/datepicker-full.min.js"></script>
    <script id="rendered-js">
        /* Bootstrap 5 JS included */
        /* vanillajs-datepicker 1.1.4 JS included */

        const getDatePickerTitle = elem => {
            // From the label or the aria-label
            const label = elem.nextElementSibling;
            let titleText = '';
            if (label && label.tagName === 'LABEL') {
                titleText = label.textContent;
            } else {
                titleText = elem.getAttribute('aria-label') || '';
            }
            return titleText;
        };

        const elems = document.querySelectorAll('.datepicker_input');
        for (const elem of elems) {
            const datepicker = new Datepicker(elem, {
                // 'format': 'dd/mm/yyyy', // UK format
                'format': 'dd-mm-yyyy', // UK format
                title: getDatePickerTitle(elem)
            });

        }
        //# sourceURL=pen.js
    </script>

    <script src="{{ asset('assets/vendors/apexcharts/apexcharts.js') }}"></script>
    <script src="{{ asset('assets/js/graphs.js') }}"></script>
</body>

</html>
