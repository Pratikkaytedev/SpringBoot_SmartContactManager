console.log("this is script file");

const toggleSidebar = () => {

	if ($(".sidebar").is(":visible")) {
		//true
		//band karna hai 

		$(".sidebar").css("display", "none");
		$(".content").css("margin-left", "0%");
	} else {
		//false
		// show karna hai
		$(".sidebar").css("display", "block");
		$(".content").css("margin-left", "20%");
	}



};

const search = () => {
//	console.log("searching");

	let query = $("#search-input").val();

	if (query === "") {
		$(".search-result").hide();
	} else {
		let url = `http://localhost:8080/search/${query}`;

//		console.log('query is ' + query);
//		console.log('url is ' + url);

		fetch(url)
			.then((response) => {
				return response.json();
			})
			.then((data) => {
//				console.log('data is ' + JSON.stringify(data));
				// Process the data or update the UI here
				let text = `<div class='list-group'>`

				data.forEach((contact) => {
					text += `<a href='/user/contact/${contact.cId}' class='list-group-item list-group-action'> ${contact.name} </a> `
				});



				text += `</div>`;
               
               $(".search-result" ).html(text);

				$(".search-result").show();
			});

	}
};

// First request to the server to create an order
const paymentStart = () => {
    console.log("Payment started");
    let amount = $("#payment_field").val();

    if (amount === "" || amount === null) {
    //    alert("Amount is required");
        swal("Failed !!", "Amount is required!!","error")
        return;
    }

   // Using Ajax to send a request to the server to create an order
    $.ajax({
        url: '/payment/create_order',
        data: JSON.stringify({ amount: amount, info: 'order_request' }),
        contentType: 'application/json',
        type: 'POST',
        dataType: 'json',
        success: function (response) {
            console.log(response);

            if (response.status === "created") {
                // Open payment form
                let options = {
                    key: 'rzp_test_D92cs1pGm8i0YF', 
                    amount: response.amount,
                    currency: 'INR',
                    name: 'Smart Contact Manager',
                    description: 'Donation',
                    image : "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSxWosnPCsC-OvcrTzjeziEpWGmuUDDGDAMCA&usqp=CAU",
                    order_id: response.id,
                    handler: function (response) {
                        console.log(response.razorpay_payment_id);
                        console.log('Payment successful');
                        
                        //update Payment on server
                        
                        updatePaymentOnServer(
							response.razorpay_payment_id,
							response.razorpay_order_id,
							"paid"
							
						);
                        
                //        alert("Congratulations! Payment successful!");
                        swal("Success !!","Congratulations! Payment successful!!","success")
                    },
                    prefill: {
                        name: '',
                        email: '',
                        contact: ''
                    },
  
                   notes: {
                        address: 'Learn Coding with Pratik'
                    },
                    theme: {
                        color: '#3399cc'
                    },
                };

                let rzp = new Razorpay(options);

                var rzp1 = new Razorpay(options);
                rzp1.on('payment.failed', function (response) {
                    console.log(response.error.code);
                    console.log(response.error.description);
                    console.log(response.error.source);
                    console.log(response.error.step);
                    console.log(response.error.reason);
                    console.log(response.error.metadata.order_id);
                    console.log(response.error.metadata.payment_id);
                 //   alert("Oops! Payment failed");
                    swal("Failed !!","Oops! Payment failed!!","error")
                });
                rzp.open();
            }
        },
        error: function (error) {
            console.log(error);
            alert("Something went wrong!!");
        }
    });
};

//
function updatePaymentOnServer(payment_id,order_id,status)
{
	$.ajax({
		url:"/payment/update_order",
		data: JSON.stringify({payment_id:payment_id,order_id:order_id,status:status,}),
		contentType: "application/json",
		type:"POST",
		dataType:"json",
		success:function(response){
       swal("Success !!","Congratulations! Payment successful!!","success")
		},
		error:function(error){
	 swal("Failed !!","Your Payment is successful, but we did not get on server, we will contact you as soon as possible!!","Failed")	
		}
	});
	
}