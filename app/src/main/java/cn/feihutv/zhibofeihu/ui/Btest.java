package cn.feihutv.zhibofeihu.ui;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/09/25
 *     desc   : 基本描述
 *     version: 1.0
 * </pre>
 */
public class Btest {

    static public Long count=0L;

    static public boolean isRun=true;

    public static  Disposable  mDisposable  ;


    public static void main(String args[]){



        System.out.println(System.currentTimeMillis()+"   start  ");


        Observable
                .interval(0,5, TimeUnit.SECONDS, Schedulers.trampoline())
//                .create(new ObservableOnSubscribe<Long>() {
//
//                    @Override
//                    public void subscribe(ObservableEmitter<Long> ob) throws Exception {
//                        try {
//
//                            while (isRun){
//                                ob.onNext(count++);
//
//                                System.out.println(System.currentTimeMillis()+"   sleep  "+count);
//                                if(count==10){
//                                    Thread.sleep(8000);
//                                }else{
//                                    Thread.sleep(5000);
//                                }
//
//                            }
//
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                })

                .timeout(10, TimeUnit.SECONDS,
                        Observable.create(new ObservableOnSubscribe<Long>() {
                            @Override
                            public void subscribe(ObservableEmitter<Long> e) throws Exception {
                                System.out.println(System.currentTimeMillis()+"   timeout  ");
                                e.onNext(100L);
                                e.onComplete();
                            }
                        })
                )

                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable =d;
                    }


                    @Override
                    public void onNext(Long aLong) {

                        if(aLong>3){
                            if(!mDisposable.isDisposed()){
                                mDisposable.dispose();
                                System.out.println(System.currentTimeMillis()+"   dispose  "+aLong);
                            }
                        }
                        System.out.println(System.currentTimeMillis()+"   end  "+aLong);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });


//                .subscribe(new Consumer<Long>() {
//                    @Override
//                    public void accept(@NonNull Long aLong) throws Exception {
//
//                        if(aLong==100) {
//                            isRun=false;
//                            System.out.println(System.currentTimeMillis()+" the  timeout   ");
//                        }
//                        System.out.println(System.currentTimeMillis()+"   end  "+aLong);
//                    }
//                });





//                .flatMap(new Function<Long, ObservableSource<Student>>() {
//                    @Override
//                    public ObservableSource<Student> apply(@NonNull Long aLong) throws Exception {
//                        System.out.println(System.currentTimeMillis()+"   start  "+aLong);
//                        if (aLong == -1) {
//                            return Observable.just(new Student(1, new String("timeout - 1"), 20));
//                        }
//                        return Observable.just(new Student(aLong.intValue(), new String("timeout - 1"), 20));
//                    }
//                })

//                .subscribe(new Consumer<Student>() {
//                    @Override
//                    public void accept(@NonNull Student student) throws Exception {
//                        System.out.println(System.currentTimeMillis()+"   end  "+student);
//                    }
//                });





//        Observable.create(new ObservableOnSubscribe<Integer>() {
//            @Override
//            public void subscribe(final ObservableEmitter<Integer> observer) throws Exception {
//
//                System.out.println(System.currentTimeMillis()+"   start  ");
//                new TEstInssetr().getTestData(new TestUIste() {
//                    @Override
//                    public void onTest(String ss) {
//                        try {
//                            if (!observer.isDisposed()) {
//                                for (int i = 1; i < 5; i++) {
//                                    observer.onNext(i);
//                                }
//                                observer.onComplete();
//                            }
//                        } catch (Exception e) {
//                            observer.onError(e);
//                        }
//                    }
//                });
//
//
//
//            }
//        }).subscribe(new Consumer<Integer>() {
//            @Override
//            public void accept(@NonNull Integer integer) throws Exception {
//                System.out.println(System.currentTimeMillis()+"  Sequence complete."+integer);
//            }
//        });


    }


    public static class Student{
        int id;
        String name;
        int age;

        public Student(int id, String name, int age) {
            this.id = id;
            this.name = name;
            this.age = age;
        }

        @Override
        public String toString() {
            return "Student{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }
    }


    public static class TEstInssetr{

        public void getTestData(final TestUIste ts){
            new Thread(new Runnable() {
                @Override
                public void run() {

                    try {
                        Thread.sleep(5000);
                        ts.onTest("888888");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        }

    }

    public interface TestUIste{
        void onTest(String ss);
    }


}
