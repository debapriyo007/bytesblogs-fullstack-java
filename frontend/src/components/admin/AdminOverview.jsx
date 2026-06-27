import { Users, BookOpen, Eye, TrendingUp, BarChart3 } from "lucide-react"
import { Card, CardContent, CardTitle, CardDescription } from "@/components/ui/card"
import LineChart from "./LineChart"
import DonutChart from "./DonutChart"
import TopBlogsChart from "./TopBlogsChart"

export default function AdminOverview({
  userCount,
  blogsTotalElements,
  totalViews,
  avgViews,
  trendData,
  categoryData,
  topBlogs,
  analyticsLoading
}) {
  if (analyticsLoading) {
    return (
      <div className="space-y-6 animate-pulse">
        {/* KPI Cards Skeleton */}
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
          {[1, 2, 3, 4].map(n => (
            <Card key={n} className="border border-zinc-200 dark:border-zinc-800 shadow-none rounded-2xl bg-white dark:bg-zinc-950">
              <CardContent className="pt-6">
                <div className="h-3 w-20 bg-muted rounded mb-2"></div>
                <div className="h-6 w-12 bg-muted rounded"></div>
              </CardContent>
            </Card>
          ))}
        </div>
        
        {/* Chart Cards Skeleton */}
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
          <Card className="border border-zinc-200 dark:border-zinc-800 shadow-none p-6 lg:col-span-2 space-y-4 rounded-3xl bg-white dark:bg-zinc-950">
            <div className="h-4 w-32 bg-muted rounded"></div>
            <div className="h-40 bg-muted rounded w-full"></div>
          </Card>
          <Card className="border border-zinc-200 dark:border-zinc-800 shadow-none p-6 space-y-4 rounded-3xl bg-white dark:bg-zinc-950">
            <div className="h-4 w-32 bg-muted rounded"></div>
            <div className="h-40 bg-muted rounded w-full"></div>
          </Card>
        </div>
      </div>
    )
  }

  return (
    <div className="space-y-6 animate-fade-in">
      {/* KPI Cards Grid */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
        {/* Total Members */}
        <Card className="border border-blue-600/20 bg-gradient-to-br from-blue-600 to-indigo-700 dark:from-blue-700 dark:to-indigo-850 shadow-none rounded-2xl transition-all duration-300 hover:-translate-y-0.5 hover:shadow-lg hover:shadow-blue-600/20 hover:from-blue-500 hover:to-indigo-600 group text-white">
          <CardContent className="pt-6 flex items-center justify-between">
            <div className="space-y-1">
              <span className="text-[10px] font-bold text-blue-100/80 uppercase tracking-wider">Total Members</span>
              <h3 className="text-2xl font-extrabold text-white">{userCount}</h3>
            </div>
            <div className="h-9 w-9 rounded-lg bg-white/15 dark:bg-white/10 flex items-center justify-center border border-white/10 transition-all duration-300 group-hover:bg-white group-hover:text-blue-600 group-hover:border-white">
              <Users className="h-4 w-4 text-white group-hover:text-blue-600 transition-colors duration-300" />
            </div>
          </CardContent>
        </Card>

        {/* Total Blogs */}
        <Card className="border border-emerald-600/20 bg-gradient-to-br from-emerald-600 to-teal-700 dark:from-emerald-700 dark:to-teal-850 shadow-none rounded-2xl transition-all duration-300 hover:-translate-y-0.5 hover:shadow-lg hover:shadow-emerald-600/20 hover:from-emerald-500 hover:to-teal-600 group text-white">
          <CardContent className="pt-6 flex items-center justify-between">
            <div className="space-y-1">
              <span className="text-[10px] font-bold text-emerald-100/80 uppercase tracking-wider">Total Blogs</span>
              <h3 className="text-2xl font-extrabold text-white">{blogsTotalElements}</h3>
            </div>
            <div className="h-9 w-9 rounded-lg bg-white/15 dark:bg-white/10 flex items-center justify-center border border-white/10 transition-all duration-300 group-hover:bg-white group-hover:text-emerald-600 group-hover:border-white">
              <BookOpen className="h-4 w-4 text-white group-hover:text-emerald-600 transition-colors duration-300" />
            </div>
          </CardContent>
        </Card>

        {/* Total Reads */}
        <Card className="border border-violet-600/20 bg-gradient-to-br from-violet-600 to-purple-700 dark:from-violet-700 dark:to-purple-850 shadow-none rounded-2xl transition-all duration-300 hover:-translate-y-0.5 hover:shadow-lg hover:shadow-violet-600/20 hover:from-violet-500 hover:to-purple-600 group text-white">
          <CardContent className="pt-6 flex items-center justify-between">
            <div className="space-y-1">
              <span className="text-[10px] font-bold text-violet-100/80 uppercase tracking-wider">Total Reads</span>
              <h3 className="text-2xl font-extrabold text-white">{totalViews.toLocaleString()}</h3>
            </div>
            <div className="h-9 w-9 rounded-lg bg-white/15 dark:bg-white/10 flex items-center justify-center border border-white/10 transition-all duration-300 group-hover:bg-white group-hover:text-violet-600 group-hover:border-white">
              <Eye className="h-4 w-4 text-white group-hover:text-violet-600 transition-colors duration-300" />
            </div>
          </CardContent>
        </Card>

        {/* Avg Views */}
        <Card className="border border-amber-600/20 bg-gradient-to-br from-amber-600 to-orange-700 dark:from-amber-700 dark:to-orange-850 shadow-none rounded-2xl transition-all duration-300 hover:-translate-y-0.5 hover:shadow-lg hover:shadow-amber-600/20 hover:from-amber-500 hover:to-orange-600 group text-white">
          <CardContent className="pt-6 flex items-center justify-between">
            <div className="space-y-1">
              <span className="text-[10px] font-bold text-amber-100/80 uppercase tracking-wider">Avg views / blog</span>
              <h3 className="text-2xl font-extrabold text-white">{avgViews}</h3>
            </div>
            <div className="h-9 w-9 rounded-lg bg-white/15 dark:bg-white/10 flex items-center justify-center border border-white/10 transition-all duration-300 group-hover:bg-white group-hover:text-amber-600 group-hover:border-white">
              <TrendingUp className="h-4 w-4 text-white group-hover:text-amber-600 transition-colors duration-300" />
            </div>
          </CardContent>
        </Card>
      </div>

      {/* Charts & Graphs Section */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Line Chart Component Card (Growth Trends) */}
        <Card className="border border-zinc-200 dark:border-zinc-800 shadow-none p-6 lg:col-span-2 space-y-4 rounded-3xl bg-white dark:bg-zinc-950">
          <div className="flex items-center justify-between border-b border-border/40 pb-3">
            <div>
              <CardTitle className="text-sm font-semibold tracking-tight flex items-center gap-1.5">
                <TrendingUp className="h-4 w-4 text-primary" />
                <span>Platform Growth Trends</span>
              </CardTitle>
              <CardDescription className="text-[10px] text-muted-foreground mt-0.5">
                User registrations and blog publications over the last 6 months.
              </CardDescription>
            </div>
            {/* Custom Legend */}
            <div className="flex items-center gap-3 text-[10px] font-semibold">
              <span className="flex items-center gap-1.5">
                <span className="h-2 w-2 rounded-full bg-foreground"></span>
                <span className="text-muted-foreground">Blogs</span>
              </span>
              <span className="flex items-center gap-1.5">
                <span className="h-2 w-2 rounded-full bg-emerald-500"></span>
                <span className="text-muted-foreground">Users</span>
              </span>
            </div>
          </div>
          
          <div className="pt-2">
            <LineChart data={trendData} />
          </div>
        </Card>

        {/* Donut Chart Component Card (Categories) */}
        <Card className="border border-zinc-200 dark:border-zinc-800 shadow-none p-6 space-y-4 rounded-3xl bg-white dark:bg-zinc-950">
          <div className="border-b border-border/40 pb-3">
            <CardTitle className="text-sm font-semibold tracking-tight flex items-center gap-1.5">
              <BarChart3 className="h-4 w-4 text-primary" />
              <span>Categories Distribution</span>
            </CardTitle>
            <CardDescription className="text-[10px] text-muted-foreground mt-0.5">
              Distribution of posts across categories.
            </CardDescription>
          </div>
          
          <div className="pt-2">
            <DonutChart data={categoryData} />
          </div>
        </Card>
      </div>

      {/* Top Performing Blogs Table / Bar Chart */}
      <Card className="border border-zinc-200 dark:border-zinc-800 shadow-none p-6 space-y-4 rounded-3xl bg-white dark:bg-zinc-950">
        <div className="border-b border-border/40 pb-3">
          <CardTitle className="text-sm font-semibold tracking-tight flex items-center gap-1.5">
            <TrendingUp className="h-4 w-4 text-indigo-500 animate-pulse" />
            <span>Top Performing Articles</span>
          </CardTitle>
          <CardDescription className="text-[10px] text-muted-foreground mt-0.5">
            Most viewed posts published on the platform.
          </CardDescription>
        </div>
        
        <div className="pt-1">
          <TopBlogsChart data={topBlogs} />
        </div>
      </Card>
    </div>
  )
}
