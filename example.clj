(require '[kaleidoscope :refer (generic-operator assign-operation)])

;; ---------------------------------------------------------
;; Basic example from README

(let [plus (generic-operator +)]
  (doto plus
    (assign-operation concat vector? vector?))
  [(plus 1 2)
   (plus [1 2 3] [4 5])])
;; => [3 (1 2 3 4 5)]


;; ---------------------------------------------------------
;; Interval example from propaganda library
;;
;; Only works for purely positive intervals

(defrecord Interval
    [lo hi])

(defn make-interval
  "Returns a new closed interval from lo to hi."
  [lo hi]
  (Interval. lo hi))

(defn mul-interval
  "Multiplies the intervals. Assumes all limits are positive."
  [x y]
  (make-interval (* (:lo x) (:lo y)) (* (:hi x) (:hi y))))

(defn div-interval
  "Divides the intervals. Assumes all limits are strictly positive."
  [x y]
  (mul-interval x (make-interval (/ 1 (:hi y)) (/ 1 (:lo y)))))

(defn square-interval
  [x]
  (make-interval (* (:lo x) (:lo x))
                 (* (:hi x) (:hi x))))

(defn sqrt-interval
  [x]
  (make-interval (Math/sqrt (double (:lo x)))
                 (Math/sqrt (double (:hi x)))))

(defn interval?
  "Returns true iff x is an interval."
  [x]
  (isa? (type x) Interval))

(defn ->interval
  "Ensures x is an interval. If x is already an interval, x is
  returned. If x is not, an interval from x to x is returned."
  [x]
  (if (interval? x)
    x
    (make-interval x x)))

(defn coercing
  "Returns a version of f that will coerce arguments using coercer
  before applying them."
  [coercer f]
  (fn [& args]
    (apply f (map coercer args))))

(def generic-mul (doto (generic-operator *)
                   (assign-operation mul-interval
                                     interval? interval?)
                   (assign-operation (coercing ->interval mul-interval)
                                     number? interval?)
                   (assign-operation (coercing ->interval mul-interval)
                                     interval? number?)))
(def generic-div (doto (generic-operator /)
                   (assign-operation div-interval
                                     interval? interval?)
                   (assign-operation (coercing ->interval div-interval)
                                     number? interval?)
                   (assign-operation (coercing ->interval div-interval)
                                     interval? number?)))
(def generic-square (doto (generic-operator (fn [x] (* x x)))
                      (assign-operation
                       square-interval
                       interval?)))
(def generic-sqrt (doto (generic-operator (fn [x] (Math/sqrt (double x))))
                    (assign-operation
                     sqrt-interval
                     interval?)))

(generic-mul 2 3)
;; => 6

(generic-mul 2 (Interval. 3 4))
;; => {:lo 6, :hi 8}

(generic-mul (Interval. 2 3) (Interval. 3 4))
;; => {:lo 6, :hi 12}

(generic-square (Interval. 3 4))
;; => {:lo 9, :hi 16}

(-> (Interval. 10 20)
    (generic-mul (Interval. 10 80))
    generic-sqrt)
;; => {:lo 10.0, :hi 40.0}
