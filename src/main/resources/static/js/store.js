/*jshint eqeqeq:false */
(function (window) {
  'use strict';

  /**
   * Creates a new client side storage object and will create an empty
   * collection if no collection already exists.
   *
   * @param {string} name The name of our DB we want to use
   * @param {function} callback Our fake DB uses callbacks because in
   * real life you probably would be making AJAX calls
   */
  function Store() {
  }

  /**
   * Finds items based on a query given as a JS object
   *
   * @param {object} query The query to match against (i.e. {foo: 'bar'})
   * @param {function} callback   The callback to fire when the query has
   * completed running
   *
   * @example
   * db.find({foo: 'bar', hello: 'world'}, function (data) {
   *	 // data will return any items that have foo: bar and
   *	 // hello: world in their properties
   * });
   */
  Store.prototype.find = function (query, callback) {
    if (!callback) {
      return;
    }

    axios.get('/todo')
      .then(function (response) {
        callback = callback || function () {
        };
        callback.call(this, response.data.filter(function (todo) {
          for (const q in query) {
            if (query[q] !== todo[q]) {
              return false;
            }
          }
          return true;
        }));
      })
      .catch(function (error) {
        console.log(error);
      })

  };

  /**
   * Will retrieve all data from the collection
   *
   * @param {function} callback The callback to fire upon retrieving data
   */
  Store.prototype.findAll = function (callback) {

    axios.get('/todo')
      .then(function (response) {
        callback = callback || function () {
        };
        callback.call(this, response.data);
      })
      .catch(function (error) {
        console.log(error);
      });


  };

  /**
   * Will save the given data to the DB. If no item exists it will create a new
   * item, otherwise it'll simply update an existing item's properties
   *
   * @param {object} updateData The data to save back into the DB
   * @param {function} callback The callback to fire after saving
   * @param {number} id An optional param to enter an ID of an item to update
   */
  Store.prototype.save = function (updateData, callback, id) {

    if (id) {
      axios({
        method: 'put',
        url:  "/todo/" + id,
        data: {
          completed: updateData.completed
        }
      }).then(function(response) {

        callback = callback || function () {};

        callback.call(this, [response.data]);
      });

    } else {

      axios.post('/todo', {
        title: updateData.title
      }).then(function (response) {
          callback = callback || function () {
          };
          callback.call(this, response.data);
        })
        .catch(function (error) {
          console.log(error);
        });
    }

  };

  /**
   * Will remove an item from the Store based on its ID
   *
   * @param {number} id The ID of the item you want to remove
   * @param {function} callback The callback to fire after saving
   */
  Store.prototype.remove = function (id, callback) {


    axios({
      method: 'DELETE',
      url:  "/todo/" + id
    }).then(function() {

      callback = callback || function () {};

      callback.call(this, [{"id" : id}]);
    });
  };

  /**
   * Will drop all storage and start fresh
   *
   * @param {function} callback The callback to fire after dropping the data
   */
  Store.prototype.drop = function (callback) {
    var todos = [];
    callback.call(this, todos);
  };

  // Export to window
  window.app = window.app || {};
  window.app.Store = Store;
})(window);