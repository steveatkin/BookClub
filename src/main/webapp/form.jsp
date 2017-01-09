<%
/*
The MIT License (MIT)

Copyright (c) 2015 IBM

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/
%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ page import="java.util.ResourceBundle" %>
<%@ page import="java.net.URL" %>
<%@ page import="java.util.ResourceBundle.Control" %>
<%@ page import="com.ibm.g11n.pipeline.client.ServiceAccount" %>
<%@ page import="com.ibm.g11n.pipeline.client.rb.CloudResourceBundle" %>
<%@ page import="com.ibm.g11n.pipeline.client.rb.CloudResourceBundleControl" %>
<%@ page import="com.ibm.g11n.pipeline.client.rb.CloudResourceBundleControl.LookupMode" %>
<%@ page import="com.ibm.globalization.Globalization" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>

<%
//ResourceBundle res=ResourceBundle.getBundle( "com.ibm.translation", request.getLocale());
ServiceAccount account = ServiceAccount.getInstance();
Control control = CloudResourceBundleControl.getInstance(account, CloudResourceBundleControl.LookupMode.LOCAL_THEN_REMOTE);
ResourceBundle res = ResourceBundle.getBundle("com.ibm.translation", request.getLocale(), control);
%>

<!DOCTYPE html>
<html lang="en">

<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>
    <%=res.getString("product")%>
  </title>

  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
  <link rel="stylesheet" href="//cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.6.3/css/bootstrap-select.min.css">
  <link rel="stylesheet" href="//cdnjs.cloudflare.com/ajax/libs/bootstrap-table/1.6.0/bootstrap-table.min.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.4.0/css/bootstrap-datepicker.standalone.min.css">

  <style type="text/css">
  <%@ include file="css/grid.css" %>
  <%@ include file="css/bookclub.css" %>
  </style>
</head>

<body>

  <nav id="myNavbar" class="navbar navbar-default navbar-inverse navbar-fixed-top" role="navigation">
    <!-- Brand and toggle get grouped for better mobile display -->
    <div class="container">
      <div class="navbar-header">
        <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#navbarCollapse">
          <span class="sr-only">Toggle navigation</span>
          <span class="icon-bar"></span>
          <span class="icon-bar"></span>
          <span class="icon-bar"></span>
        </button>
        <a class="navbar-brand" href="#">
          <%=res.getString("product")%>
        </a>
      </div>
      <!-- Collect the nav links, forms, and other content for toggling -->
      <div class="collapse navbar-collapse" id="navbarCollapse">
        <ul class="nav navbar-nav">
          <li>
            <a href="index.jsp">
              <%=res.getString("home")%>
            </a>
          </li>
          <li class="active">
            <a href="form.jsp">
              <%=res.getString("start")%>
                   </a>
                 </li>
               </ul>
             </div>
           </div>
         </nav>
         <div class="container">

           <div class="page-header">
             <h1><small><%=res.getString("explore_ny")%></small></h1>
           </div>

           <label for="tranlslation" class="control-label"><%=res.getString("enable_translation")%></label>
           <div class="form-group">
           <select class="selectpicker form-control" data-width="auto" id="translation">
             <option value="true">
             <%=res.getString("yes")%>
             </option>
             <option value="false" selected="selected">
             <%=res.getString("no")%>
             </option>
           </select>
           </div>


           <label for="datepicker" class="control-label"><%=res.getString("date")%></label>
    <div class="form-group">
      <div class="input-append date" id="datePicker">
        <input class="span2" size="18" type="text">
        <span class="add-on"><i class="glyphicon glyphicon-th"></i></span>
      </div>
    </div>

    <label for="bestSellerList" class="control-label"><%=res.getString("book_list")%></label>
    <div class="form-group">
      <select class="selectpicker form-control" data-width="auto" id="bestSellerList" title='<%=res.getString("select_list")%>'>
        <option data-hidden="true"></option>
        <option value="combined-print-and-e-book-fiction">
          <%=res.getString("combined_fiction")%>
        </option>
        <option value="combined-print-and-e-book-nonfiction">
          <%=res.getString("combined_nonfiction")%>
        </option>
        <option value="hardcover-fiction">
          <%=res.getString("hardcover_fiction")%>
        </option>
        <option value="hardcover-nonfiction">
          <%=res.getString("hardcover_nonfiction")%>
        </option>
        <option value="trade-fiction-paperback">
          <%=res.getString("paperback_fiction")%>
        </option>
      </select>

    </div>

    <div class="panel panel-default">
      <div class="panel-heading">
        <h3 class="panel-title"><%=res.getString("panel_ny_times")%></h3>
      </div>

             <div class="panel-body">
               <div class="panel-group" id="accordionPanel">
                 <div class="accordion" id="accordion" style="display: none;">
                   <% for(int i=0 ; i < 20; ++i) { %>
                       <div class="panel panel-default">
                         <div class="panel-heading">
                           <h4 class="panel-title">
							<a id="anchor<%=i%>" data-toggle="collapse" data-parent="#accordion" href="#accordion<%=i%>"></a>
							</h4>
                          </div>
                          <div id="accordion<%=i%>" class="panel-collapse collapse" data-book="" data-number=<%=i%>>
                  <div class="panel-body">

                    <div class="row-fluid">
                      <div class="col-md-10 column-white">
                        <span class="label label-primary"><%=res.getString("description")%></span>
                              <p id="description<%=i%>"></p>
                      </div>
                      <div class="col-md-2 column-white">
                        <span class="label label-primary"><%=res.getString("weeks")%></span>
                              <p id="weeks<%=i%>"></p>
                      </div>
                    </div>

                    <div class="row-fluid">
                      <div class="col-md-12 column-white">
                        <span class="label label-primary"><%=res.getString("dream_books")%></span>
                             <table data-toggle="table" class="table" id="criticTable<%=i%>">
                          <thead>
                            <tr>
                              <th data-field="snippet">
                                <%=res.getString("snippet")%>
                              </th>
                              <th data-field="source">
                                <%=res.getString("source")%>
                              </th>
                              <th data-field="polarity" data-formatter="polarityFormatter">
                                <%=res.getString("polarity")%>
                              </th>
                              <th data-field="link" data-visible="false"></th>
                            </tr>
                          </thead>
                        </table>
                      </div>
                    </div>

                    <div class="row-fluid">
                      <div class="col-md-12 column-white">
                        <span class="label label-primary"><%=res.getString("word_cloud")%></span>
                             <div id="cloud<%=i%>">
                        </div>
                      </div>
                    </div>

                    <div class="row-fluid">
                      <div class="col-md-12 column-white">
                        <span class="label label-primary"><%=res.getString("twitter")%></span>
                             <table data-toggle="table" class="table" id="table<%=i%>">
                          <thead>
                            <tr>
                              <th data-field="screenName">
                                <%=res.getString("screen_name_table")%>
                              </th>
                              <th data-field="tweet" data-formatter="twitterFormatter">
                                <%=res.getString("message_table")%>
                              </th>
                              <th data-field="sentiment" data-formatter="polarityFormatter">
                                <%=res.getString("sentiment_label")%>
                              </th>
                            </tr>
                          </thead>
                          <tbody>
                          </tbody>
                        </table>
                      </div>
                    </div>

                  </div>
                </div>
              </div>
              <% } %>

          </div>
        </div>
      </div>

    </div>
  </div>

  <div id='ajax_loader' style="position: fixed; left: 50%; top: 50%; display: none;">
    <img src="${pageContext.request.contextPath}/images/ajax-loader.gif" />
  </div>

  <script src="//code.jquery.com/jquery-1.11.2.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
  <script src="//cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.6.3/js/bootstrap-select.min.js"></script>
  <script src="//cdnjs.cloudflare.com/ajax/libs/bootstrap-table/1.6.0/bootstrap-table.min.js"></script>

  <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.4.0/js/bootstrap-datepicker.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.4.0/locales/bootstrap-datepicker.zh-CN.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.4.0/locales/bootstrap-datepicker.zh-TW.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.4.0/locales/bootstrap-datepicker.ja.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.4.0/locales/bootstrap-datepicker.fr.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.4.0/locales/bootstrap-datepicker.es.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.4.0/locales/bootstrap-datepicker.it.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.4.0/locales/bootstrap-datepicker.pt-BR.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.4.0/locales/bootstrap-datepicker.de.min.js"></script>

  <script type="text/javascript">
  <%@ include file = "js/eventsource.min.js" %>
  <%@ include file = "js/jqTagCloud.js" %>
  </script>

  <script>

  function polarityFormatter(value) {
  	var span = "";

    if (value.toLowerCase() === 'positive') {
    	span = "<span class='badge badge-positive'>+</span>";
    } else if (value.toLowerCase() === 'negative') {
        span = "<span class='badge badge-negative'>-</span>";
    }
    else {
        span = "<span class='badge badge-neutral'>?</span>";
    }
    return span;
  }

  function twitterFormatter(value) {
  	return '<a target="_blank" href="' + value.link + '">' + value.message + '</a>';
  }


  function setupTwitterEventSource(IdNum) {
    if (typeof(EventSource) !== 'undefined') {
      // Remove all the entries from the table
      var tableId = '#table' + IdNum;
      $(tableId).bootstrapTable('load', []);

	  var enable = $('#translation').val();

      var jsonData = $('#accordion' + IdNum).data('book');
      var book = JSON.parse(jsonData);

      var source = new EventSource('Twitter?title=' + book.title + '&author=' + book.author + '&enable=' + enable);

      source.onmessage = function(event) {
        var tweet = JSON.parse(event.data);

        $(tableId).bootstrapTable('append', [tweet]);
      };

      source.onerror = function(event) {
      	// alert('<%=StringEscapeUtils.escapeJavaScript(res.getString("closed"))%>');
      };

      source.addEventListener('finished', function(event) {
        source.close();
      }, false);
    } else {
      // alert('<%=StringEscapeUtils.escapeJavaScript(res.getString("sse_error"))%>');
    }
    return false;
  }


  function setupConceptEventSource(IdNum, url) {
    if (typeof(EventSource) !== 'undefined') {
      var enable = $('#translation').val();
      var words = new Array();
      var source = new EventSource('Concept?url=' + url + '&enable=' + enable);

      // Cleanup the word cloud list
      $('#cloud' + IdNum).empty();

      source.onmessage = function(event) {
        var concept = JSON.parse(event.data);
        words.push({
          text: concept.concept,
          weight: concept.score
        });
      };

      source.onerror = function(event) {
      	alert('<%=StringEscapeUtils.escapeJavaScript(res.getString("closed"))%>');
      };

      source.addEventListener('finished', function(event) {
        for (i = 0; i < words.length; ++i) {
          $('#cloud' + IdNum).append('<span class="word-cloud" count="' + (words[i].weight * 100 | 0) + '">' + words[i].text + '</span>');
        }

        $('#cloud' + IdNum).jqTagCloud({
          maxSize: 32,
          minSize: 14
        });
        source.close();
      }, false);
    } else {
      alert('<%=StringEscapeUtils.escapeJavaScript(res.getString("sse_error"))%>');
    }
    return false;
  }


  function setupReviewEventSource(IdNum) {
    if (typeof(EventSource) !== 'undefined') {
      // Remove all the entries from the table
      var tableId = '#criticTable' + IdNum;
      $(tableId).bootstrapTable('load', []);

      var enable = $('#translation').val();
      var jsonData = $('#accordion' + IdNum).data('book');
      var book = JSON.parse(jsonData);
      var source = new EventSource('Review?title=' + book.title + '&enable=' + enable);

      source.onmessage = function(event) {
        var review = JSON.parse(event.data);
        $(tableId).bootstrapTable('append', [review]);
      };

      source.onerror = function(event) {
      	alert('<%=StringEscapeUtils.escapeJavaScript(res.getString("closed"))%>');
      };

      source.addEventListener('synopsis', function(event) {
      	var synopsisLink = JSON.parse(event.data);
      	setupConceptEventSource(IdNum, synopsisLink.synopsis_link);
      }, false);

      source.addEventListener('finished', function(event) {
        source.close();
        var reviews = $(tableId).bootstrapTable('getData');
      }, false);
    } else {
      alert('<%=StringEscapeUtils.escapeJavaScript(res.getString("sse_error"))%>');
    }
    return false;
  }

  // Load the translated string to show in the table when there is no data
  $.extend($.fn.bootstrapTable.defaults, {
  	formatNoMatches: function() {
            return '<%=StringEscapeUtils.escapeJavaScript(res.getString("no_data"))%>';
        }
    });

  // wait for the DOM to be loaded
  $(document).ready(function() {

    var currentDate = new Date();
    var mm = currentDate.getMonth();
    var dd = currentDate.getDate();
    var yyyy = currentDate.getFullYear();

    $('#datePicker').datepicker({
      language: '<%=request.getLocale().toLanguageTag()%>',
      autoclose: true,
      orientation: "top",
      endDate: currentDate
    });

    $('#datePicker').datepicker('setDate', new Date(yyyy, mm, dd));

    $('#datePicker').on('changeDate', function(e) {
      currentDate = new Date(e.date);
      $('#bestSellerList').trigger("change");
    });

    $('#translation').change(function() {
      $('#bestSellerList').trigger("change");
    });

    // When an accordion panel is opened make the request for the details
    $('#accordion').on('show.bs.collapse', function(e) {
      var IdNum = $('#' + e.target.id).data('number');
      var jsonData = $('#accordion' + IdNum).data('book');
      var book = JSON.parse(jsonData);

      var enable = $('#translation').val();

      // Add the description to the accordion panel
      if(enable === 'true') {
      	$.post('Watson',
      		{text: book.description},
      		function(data, status) {
      			if(status === 'success') {
      				$('#description' + IdNum).text(data);
      			}
      			else {
      				alert('<%=StringEscapeUtils.escapeJavaScript(res.getString("closed"))%>');
      			}
      		});
      }
      else {
      	$('#description' + IdNum).text(book.description);
      }

      setupTwitterEventSource(IdNum);
      setupReviewEventSource(IdNum);
    });

    // Initialize the select picker list
    $('.selectpicker').selectpicker();

    // Listen for the selection of the best sellers list
    $('#bestSellerList').change(function() {
      var list = $('#bestSellerList').val();

      // If we have a selected item
      if(list !== "") {
      	var mm = ('0' + (currentDate.getMonth() + 1)).slice(-2);
      	var dd = ('0' + currentDate.getDate()).slice(-2);
      	var yyyy = currentDate.getFullYear();
      	var selectedDate = yyyy + '-' + mm + '-' + dd;

      	// Close any open panel before getting a list
      	$('.panel-collapse.in').each(function() {
        	$(this).collapse('hide');
      	});

      	// Call the servlet to get back the books for a list
      	$.ajax({
        	url: 'BestSellers',
      		type: 'GET',
      		data: {list: list,
      		   	date: selectedDate},
      		beforeSend: function() {
      			// Show the loading gif
      			$('#ajax_loader').show();
      			// Hide the list of boxes while we are updating
      			$('#accordion').hide();
      		},
        	success: function(data) {
        		for (var i = 0; i < data.length; ++i) {
          			var title = data[i].book_details[0].title;
          			var author = data[i].book_details[0].author;
          			var weeks = data[i].weeks_on_list;

          			// Update the archor tag with the book info
          			$('#anchor' + i).text(title + ' \u2014 ' + author);

          			$('#weeks' + i).text(weeks);

          			// Place the book object into the data-book HTML attribute
          			var value = JSON.stringify(data[i].book_details[0]);
          			$('#accordion' + i).data('book', value);
        		}
        		// Show the list of books
        		$('#accordion').show();
        	},
        	error: function(xhr) {
        		alert('<%=StringEscapeUtils.escapeJavaScript(res.getString("ny_times_error"))%>');
        	},
        	complete: function() {
        		// Hide the loading gif
        		$('#ajax_loader').hide();
        	},
        	dataType: 'json',
        	timeout: 30000,
      	});
      }

    });

  });
  </script>
</body>

</html>
